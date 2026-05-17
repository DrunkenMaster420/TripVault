package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,Long> {
}
