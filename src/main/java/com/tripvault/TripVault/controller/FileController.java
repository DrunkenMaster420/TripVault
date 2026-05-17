package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public String upload(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal User user
    ) throws Exception {

        fileService.uploadFile(file, user);

        return "Uploaded";
    }
}