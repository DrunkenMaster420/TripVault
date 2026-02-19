package com.tripvault.TripVault.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TripMemberResponse {

    private Long userId;
    private String username;
    private String name;

    private String role;

    private Long allocatedBytes;
    private Long usedBytes;

    private LocalDateTime joinedAt;

    private Boolean isActive;

}
