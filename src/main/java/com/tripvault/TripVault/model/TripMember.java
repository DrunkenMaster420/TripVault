package com.tripvault.TripVault.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_members")
@Data
public class TripMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id",nullable = false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TripRole role;

    @Column(name = "allocated_bytes",nullable = false)
    private Long allocatedBytes;

    @Column(name = "used_bytes",nullable = false)
    private Long usedBytes=0L;

    @Column(name = "joined_at",updatable = false)
    private LocalDateTime joinedAt;

    private Boolean isActive=true;
}
