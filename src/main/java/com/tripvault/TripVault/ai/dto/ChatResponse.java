package com.tripvault.TripVault.ai.dto;

import lombok.Data;

@Data
public class ChatResponse {
    private String content;

    private String model;

    private TokenUsage usage;

    private FinishReason finishReason;
}
