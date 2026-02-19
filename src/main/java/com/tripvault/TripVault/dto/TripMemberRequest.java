package com.tripvault.TripVault.dto;

import lombok.Data;

@Data
public class TripMemberRequest {

    private Long userId;
    private Long allocatedBytes;
}
