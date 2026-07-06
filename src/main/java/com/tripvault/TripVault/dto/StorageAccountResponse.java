package com.tripvault.TripVault.dto;

import lombok.Data;

@Data
public class StorageAccountResponse {

    private Long id;
    private String googleEmail;
    private Long usedQuota;
    private Long totalQuota;
    private boolean active;

    // getters & setters
}