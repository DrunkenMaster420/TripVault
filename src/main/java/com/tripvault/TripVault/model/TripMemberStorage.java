package com.tripvault.TripVault.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "trip_member_storage",
        uniqueConstraints = @UniqueConstraint(columnNames = "trip_member_id")
)
@Data
public class TripMemberStorage  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_member_id", nullable = false)
    private Long tripMemberId;

    @Column(name = "allocated_bytes", nullable = false)
    private Long allocatedBytes;

    @Column(name = "used_bytes", nullable = false)
    private Long usedBytes = 0L;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
