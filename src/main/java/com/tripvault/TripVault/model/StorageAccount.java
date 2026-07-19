package com.tripvault.TripVault.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "storage_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"owner_user_id", "google_id"}
                )
        }
)
public class StorageAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner;

    private String googleEmail;

    @Column(nullable = false)
    private String googleId;

    @Column(length = 3000)
    private String accessToken;

    @Column(length = 3000)
    private String refreshToken;

    private Long totalQuota;

    private Long usedQuota;

    private Boolean active;

    private LocalDateTime tokenExpiry;
}
