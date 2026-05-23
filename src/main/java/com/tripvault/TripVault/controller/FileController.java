package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final UserRepository userRepository;

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
}