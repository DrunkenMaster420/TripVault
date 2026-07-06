package com.tripvault.TripVault.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileResponse {

    private Long id;

    private String fileName;

    private Long fileSize;

    private String contentType;

    private LocalDateTime uploadedAt;
}