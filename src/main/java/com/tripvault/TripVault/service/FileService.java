package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.File;
import com.tripvault.TripVault.model.FileChunk;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.FileChunkRepository;
import com.tripvault.TripVault.repository.FileRepository;
import com.tripvault.TripVault.storage.ChunkSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileChunkRepository chunkRepository;
    private final GoogleDriveService googleDriveService;

    public void uploadFile(MultipartFile multipartFile, User user) throws Exception {

        File file = new File();

        file.setFileName(multipartFile.getOriginalFilename());
        file.setFileSize(multipartFile.getSize());
        file.setContentType(multipartFile.getContentType());
        file.setUploadedAt(LocalDateTime.now());
        file.setUser(user);

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
            chunkRepository.save(fileChunk);

            index++;
        }
    }
}