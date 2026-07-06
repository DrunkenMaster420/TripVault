package com.tripvault.TripVault.service;

import com.tripvault.TripVault.dto.FileResponse;
import com.tripvault.TripVault.model.*;
import com.tripvault.TripVault.repository.*;
import com.tripvault.TripVault.storage.StreamingChunkReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileChunkRepository fileChunkRepository;
    private final FileChunkRepository chunkRepository;
    private final GoogleDriveService googleDriveService;
    private final TripRepository tripRepository;
    private final TripMemberRepository tripMemberRepository;
    private final StorageAllocationService storageAllocationService;
    private final StorageAccountRepository storageAccountRepository;
    private final ExecutorService uploadExecutor;

    public void uploadFile(
            MultipartFile multipartFile,
            Long tripId,
            User user
    ) throws Exception {

        validateTripMembership(
                tripId,
                user.getId()
        );

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        File file = new File();

        file.setFileName(
                multipartFile.getOriginalFilename()
        );
        file.setFileSize(
                multipartFile.getSize()
        );
        file.setContentType(
                multipartFile.getContentType()
        );
        file.setUploadedAt(
                LocalDateTime.now()
        );
        file.setUser(user);
        file.setTrip(trip);

        file = fileRepository.save(file);

        InputStream inputStream =
                multipartFile.getInputStream();

        List<CompletableFuture<Void>> futures =
                new ArrayList<>();

        int index = 0;

        while (true) {

            byte[] chunk =
                    StreamingChunkReader.readChunk(
                            inputStream
                    );

            if (chunk == null) {
                break;
            }

            final int chunkIndex = index;
            final byte[] chunkData = chunk;
            final File savedFile = file;

            CompletableFuture<Void> future =
                    CompletableFuture.runAsync(() -> {

                        StorageAccount storageAccount = null;

                        try {

                            storageAccount =
                                    storageAllocationService.allocate(
                                            (long) chunkData.length
                                    );

                            String chunkFileName =
                                    savedFile.getFileName()
                                            + "_chunk_"
                                            + chunkIndex;

                            String driveFileId =
                                    googleDriveService.uploadChunk(
                                            chunkData,
                                            chunkFileName,
                                            storageAccount
                                    );

                            FileChunk fileChunk =
                                    new FileChunk();

                            fileChunk.setChunkIndex(
                                    chunkIndex
                            );

                            fileChunk.setChunkSize(
                                    (long) chunkData.length
                            );

                            fileChunk.setDriveFileId(
                                    driveFileId
                            );

                            fileChunk.setFile(
                                    savedFile
                            );

                            fileChunk.setStorageAccount(
                                    storageAccount
                            );

                            chunkRepository.save(
                                    fileChunk
                            );

                            System.out.println(
                                    "Uploaded chunk "
                                            + chunkIndex
                                            + " to account "
                                            + storageAccount.getId()
                            );

                        } catch (Exception e) {

                            if (storageAccount != null) {

                                synchronized (storageAllocationService) {

                                    storageAccount.setUsedQuota(
                                            storageAccount.getUsedQuota()
                                                    - chunkData.length
                                    );

                                    storageAccountRepository.save(
                                            storageAccount
                                    );
                                }
                            }

                            throw new RuntimeException(e);
                        }

                    }, uploadExecutor);

            futures.add(future);

            index++;
        }

        try {

            CompletableFuture.allOf(
                    futures.toArray(
                            new CompletableFuture[0]
                    )
            ).join();

        } catch (Exception e) {

            fileRepository.delete(file);

            throw new RuntimeException(
                    "Upload failed",
                    e
            );
        }

        System.out.println(
                "File upload completed successfully"
        );
    }

    public void downloadFile(Long fileId, User user, OutputStream outputStream)throws IOException {



         File file=fileRepository.findById(fileId).orElseThrow(()->new RuntimeException("File Not Found"));

        validateTripMembership(
                file.getTrip().getId(),
                user.getId()
        );

         List<FileChunk> chunks=fileChunkRepository.findByFileOrderByChunkIndexAsc(file);

        System.out.println("Chunks found: " + chunks.size());

        for(FileChunk chunk:chunks){
            System.out.println("Downloading chunk: " + chunk.getChunkIndex());
            try {

                System.out.println(
                        "Chunk "
                                + chunk.getChunkIndex()
                                + " Account "
                                + chunk.getStorageAccount().getId()
                );

                System.out.println(
                        "START chunk " +
                                chunk.getChunkIndex()
                );

                googleDriveService.downloadChunk(
                        chunk.getDriveFileId(),
                        chunk.getStorageAccount(),
                        outputStream
                );

                System.out.println(
                        "END chunk " +
                                chunk.getChunkIndex()
                );

                System.out.println("Chunk downloaded successfully");

                System.out.println("Chunk written");

            }catch (Exception e) {

                System.out.println(
                        "Failed chunk index: "
                                + chunk.getChunkIndex()
                );

                System.out.println(
                        "Drive file id: "
                                + chunk.getDriveFileId()
                );

                throw e;
            }
        }
    }

    public File getFileById(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(()->new RuntimeException("File Not Found"));
    }

    public List<FileResponse> getFiles(Long tripId) {

        System.out.println("Service reached");

        return fileRepository.findByTrip_Id(tripId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deleteFile(Long fileId, User user) {

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        validateTripMembership(
                file.getTrip().getId(),
                user.getId()
        );

        if (!file.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Only uploader can delete this file"
            );
        }

        List<FileChunk> chunks =
                fileChunkRepository.findByFileOrderByChunkIndexAsc(file);

        for (FileChunk chunk : chunks) {

            System.out.println("Deleting chunk: " + chunk.getChunkIndex());

            StorageAccount storageAccount =
                    chunk.getStorageAccount();

            googleDriveService.deleteChunk(
                    chunk.getDriveFileId(),
                    storageAccount
            );

            storageAccount.setUsedQuota(
                    storageAccount.getUsedQuota()
                            - chunk.getChunkSize()
            );

            storageAccountRepository.save(storageAccount);

            System.out.println("Chunk deleted from Google Drive");
        }

        fileChunkRepository.deleteByFile(file);

        System.out.println("Chunk metadata deleted");

        fileRepository.delete(file);

        System.out.println("File metadata deleted");
    }

    private void validateTripMembership(
            Long tripId,
            Long userId
    ) {

        boolean member =
                tripMemberRepository
                        .existsByTrip_IdAndUser_IdAndIsActiveTrue(
                                tripId,
                                userId
                        );

        if (!member) {
            throw new RuntimeException(
                    "Access denied"
            );
        }
    }

    private FileResponse mapToResponse(File file) {

        FileResponse response = new FileResponse();

        response.setId(file.getId());
        response.setFileName(file.getFileName());
        response.setFileSize(file.getFileSize());
        response.setContentType(file.getContentType());
        response.setUploadedAt(file.getUploadedAt());

        return response;
    }
}