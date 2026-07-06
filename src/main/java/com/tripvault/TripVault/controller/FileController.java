package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.File;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.FileService;
import com.tripvault.TripVault.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.tripvault.TripVault.dto.FileResponse;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/trip/{tripId}")
    public List<FileResponse> getFiles(@PathVariable Long tripId) {

        return fileService.getFiles(tripId);
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tripId") Long tripId
    ) throws Exception {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        fileService.uploadFile(file, tripId,user);

        return "Uploaded";
    }

    @GetMapping("/download/{fileId}")
    public void downloadFile(
            @PathVariable Long fileId,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        User user = userService.findByUsername(
                authentication.getName()
        );

        File file = fileService.getFileById(fileId);

        response.setContentType(
                file.getContentType()
        );

        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" +
                        file.getFileName() +
                        "\""
        );

        fileService.downloadFile(
                fileId,
                user,
                response.getOutputStream()
        );

        response.flushBuffer();
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