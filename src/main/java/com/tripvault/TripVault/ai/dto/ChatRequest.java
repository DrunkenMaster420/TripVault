package com.tripvault.TripVault.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    private List <ChatMessage> messages;

    private String model;

    private Double temperature;

    private Integer maxTokens;

    private Boolean stream;
}
