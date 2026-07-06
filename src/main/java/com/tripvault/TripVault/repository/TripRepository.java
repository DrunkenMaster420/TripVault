package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip,Long> {
    List<Trip> findByCreatedBy_Id(Long userId);

//    boolean existsByTrip_IdAndUser_IdAndIsActiveTrue(Long tripId, Long userId);

}
