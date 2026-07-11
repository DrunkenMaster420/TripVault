package com.tripvault.TripVault.ai.dto;

import lombok.Data;

@Data
public class ChatMessage {
    private MessageRole role;

    private String content;
}
