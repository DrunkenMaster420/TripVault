package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.File;
import com.tripvault.TripVault.model.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileChunkRepository extends JpaRepository<FileChunk,Long   > {
    List<FileChunk> findByFileOrderByChunkIndexAsc(File file);
    void deleteByFile(File file);
}
