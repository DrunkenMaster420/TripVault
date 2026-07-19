package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.TripMember;
import com.tripvault.TripVault.model.TripMemberStorage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TripMemberStorageRepository extends JpaRepository<TripMemberStorage,Long> {

    Optional<TripMemberStorage> findByTripMember(TripMember tripMember);

    @Modifying
    @Transactional
    @Query("""
UPDATE TripMemberStorage s
SET s.usedBytes = s.usedBytes + :size,
    s.updatedAt = CURRENT_TIMESTAMP
WHERE s.tripMember.id = :tripMemberId
AND s.usedBytes + :size <= s.allocatedBytes
""")
    int incrementUsedBytes(Long tripMemberId, Long size);

    @Modifying
    @Transactional
    @Query("""
UPDATE TripMemberStorage s
SET s.usedBytes = s.usedBytes - :size,
    s.updatedAt = CURRENT_TIMESTAMP
WHERE s.tripMember.id = :tripMemberId
AND s.usedBytes >= :size
""")
    int decrementUsedBytes(Long tripMemberId, Long size);

}
