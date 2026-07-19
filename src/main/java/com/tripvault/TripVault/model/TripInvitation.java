package com.tripvault.TripVault.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "trip_invitations")
@Data
public class TripInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private Long allocatedStorage;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime respondedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        status = InvitationStatus.PENDING;
    }
}