package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.TripMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripMemberRepository extends JpaRepository<TripMember,Long> {
    Optional<TripMember> findByTrip_IdAndUser_Id(Long tripId,Long userId);
    List<TripMember> findByTrip_Id(Long tripId);
}
