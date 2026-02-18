package com.tripvault.TripVault.model;

import lombok.Data;

@Data
public class TripMemberRequest {

    private Long userId;
    private Long allocatedBytes;
}
