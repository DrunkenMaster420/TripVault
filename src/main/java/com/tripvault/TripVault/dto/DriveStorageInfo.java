package com.tripvault.TripVault.dto;

import lombok.Data;

@Data
public class DriveStorageInfo {
    private Long totalQuota;
    private Long usedQuota;
}
