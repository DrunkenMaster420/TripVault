package com.tripvault.TripVault.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private Long fileSize;

    private String contentType;

    private LocalDateTime uploadedAt;

    @ManyToOne
    private User user;
}
