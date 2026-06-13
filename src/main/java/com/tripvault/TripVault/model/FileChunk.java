package com.tripvault.TripVault.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "file_chunks")
@Data
public class FileChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int chunkIndex;

    private Long chunkSize;

    private String driveFileId;


    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_account_id")
    private StorageAccount storageAccount;
}
