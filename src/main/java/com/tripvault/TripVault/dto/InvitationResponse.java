package com.tripvault.TripVault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InvitationResponse {

    private Long id;

    private Long tripId;

    private String tripName;

    private String senderName;

    private Long allocatedQuota;

    private LocalDateTime createdAt;
}
