package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileChunkRepository extends JpaRepository<FileChunk,Long   > {
}
