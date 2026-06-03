package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.File;
import com.tripvault.TripVault.model.FileChunk;
import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.FileChunkRepository;
import com.tripvault.TripVault.repository.FileRepository;
import com.tripvault.TripVault.repository.TripMemberRepository;
import com.tripvault.TripVault.repository.TripRepository;
import com.tripvault.TripVault.storage.ChunkSplitter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

        List<byte[]> chunks = ChunkSplitter.splitFile(multipartFile);

        int index = 0;

        for (byte[] chunk : chunks) {
            String chunkFileName =
                    file.getFileName() + "_chunk_" + index;

            String driveFileId =
                    googleDriveService.uploadChunk(
                            chunk,
                            chunkFileName,
                            user
                    );

            FileChunk fileChunk = new FileChunk();

            fileChunk.setChunkIndex(index);
            fileChunk.setChunkSize((long) chunk.length);
            fileChunk.setDriveFileId(driveFileId);
            fileChunk.setFile(file);
            fileChunk.setStorageOwner(user);
            System.out.println("Saving chunk " + index);
            chunkRepository.save(fileChunk);
            System.out.println("Saved chunk " + index);

            index++;
        }
    }

    public byte[] downloadFile(Long fileId,User user)throws IOException {



         File file=fileRepository.findById(fileId).orElseThrow(()->new RuntimeException("File Not Found"));

        validateTripMembership(
                file.getTrip().getId(),
                user.getId()
        );

         List<FileChunk> chunks=fileChunkRepository.findByFileOrderByChunkIndexAsc(file);

        System.out.println("Chunks found: " + chunks.size());
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

        for(FileChunk chunk:chunks){
            System.out.println("Downloading chunk: " + chunk.getChunkIndex());
            try {

                byte[] chunkData = googleDriveService.downloadChunk(
                        chunk.getDriveFileId(),
                        chunk.getStorageOwner()
                );

                System.out.println("Chunk downloaded successfully");

                outputStream.write(chunkData);
                System.out.println("Chunk written");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outputStream.toByteArray();
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

            googleDriveService.deleteChunk(
                    chunk.getDriveFileId(),
                    chunk.getStorageOwner()
            );

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