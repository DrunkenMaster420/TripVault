package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {
    List<File> findByTrip_Id(Long tripId);
}
