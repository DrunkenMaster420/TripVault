package com.tripvault.TripVault.service;

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
import java.util.List;

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

    public void uploadFile(MultipartFile multipartFile,Long tripId ,User user) throws Exception {

        validateTripMembership(
                tripId,
                user.getId()
        );

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        File file = new File();

        file.setFileName(multipartFile.getOriginalFilename());
        file.setFileSize(multipartFile.getSize());
        file.setContentType(multipartFile.getContentType());
        file.setUploadedAt(LocalDateTime.now());
        file.setUser(user);
        file.setTrip(trip);

        file = fileRepository.save(file);


        InputStream inputStream =
                multipartFile.getInputStream();

        int index = 0;

        while (true) {

            byte[] chunk =
                    StreamingChunkReader.readChunk(
                            inputStream
                    );

            if (chunk == null) {
                break;
            }

            String chunkFileName =
                    file.getFileName()
                            + "_chunk_"
                            + index;

            StorageAccount storageAccount =
                    storageAllocationService.allocate(
                            (long) chunk.length
                    );

            String driveFileId =
                    googleDriveService.uploadChunk(
                            chunk,
                            chunkFileName,
                            storageAccount
                    );

            storageAccount.setUsedQuota(
                    storageAccount.getUsedQuota()
                            + chunk.length
            );

            storageAccountRepository.save(storageAccount);

            FileChunk fileChunk =
                    new FileChunk();

            fileChunk.setChunkIndex(index);
            fileChunk.setChunkSize(
                    (long) chunk.length
            );
            fileChunk.setDriveFileId(
                    driveFileId
            );
            fileChunk.setFile(file);
            fileChunk.setStorageAccount(
                    storageAccount
            );

            chunkRepository.save(fileChunk);

            index++;
        }
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

                googleDriveService.downloadChunk(
                        chunk.getDriveFileId(),
                        chunk.getStorageAccount(),
                        outputStream
                );

                System.out.println("Chunk downloaded successfully");

                System.out.println("Chunk written");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public File getFileById(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(()->new RuntimeException("File Not Found"));
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
}