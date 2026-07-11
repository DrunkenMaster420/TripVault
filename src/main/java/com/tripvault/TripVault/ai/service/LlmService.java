package com.tripvault.TripVault.ai.service;

import com.tripvault.TripVault.ai.dto.ChatRequest;
import com.tripvault.TripVault.ai.dto.ChatResponse;

public interface LlmService {

    ChatResponse chat(ChatRequest request);
}
