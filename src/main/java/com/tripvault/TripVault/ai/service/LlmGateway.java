package com.tripvault.TripVault.ai.service;

public interface LlmGateway {

    String generate(
            String systemPrompt,
            String userPrompt
    );
}
