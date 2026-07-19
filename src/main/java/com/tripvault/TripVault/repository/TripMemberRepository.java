package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.Trip;
import com.tripvault.TripVault.model.TripMember;
import com.tripvault.TripVault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripMemberRepository extends JpaRepository<TripMember,Long> {
    Optional<TripMember> findByTrip_IdAndUser_Id(Long tripId,Long userId);
    List<TripMember> findByTrip_Id(Long tripId);
    boolean existsByTrip_IdAndUser_IdAndIsActiveTrue(
            Long tripId,
            Long userId
    );

    List<TripMember> findByUser_IdAndIsActiveTrue(Long userId);

    boolean existsByTripAndUser(Trip trip, User user);

    List<TripMember> findByTripId(Long tripId);
}
