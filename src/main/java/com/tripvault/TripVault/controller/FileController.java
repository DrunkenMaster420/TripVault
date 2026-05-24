package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.File;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.FileService;
import com.tripvault.TripVault.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/upload")
    public String upload(
            @RequestParam MultipartFile file
    ) throws Exception {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        fileService.uploadFile(file, user);

        return "Uploaded";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId,Authentication authentication)throws IOException {
        User user=userService.findByUsername(authentication.getName());
        byte[] fileData=fileService.downloadFile(fileId,user);
        File file=fileService.getFileById(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment,filename=\""+file.getFileName()+"\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long fileId,
            Authentication authentication
    ) {

        User user = userService.findByUsername(
                authentication.getName()
        );

        fileService.deleteFile(fileId, user);

        return ResponseEntity.ok("File deleted successfully");
    }
}