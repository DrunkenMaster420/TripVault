package com.tripvault.TripVault.ai.service;

import com.tripvault.TripVault.ai.dto.ChatRequest;
import com.tripvault.TripVault.ai.dto.ChatResponse;
import com.tripvault.TripVault.ai.prompt.PromptManager;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final LlmGateway llmGateway;
    private final PromptManager promptManager;

    public AiService(LlmGateway llmGateway, PromptManager promptManager) {
        this.llmGateway = llmGateway;
        this.promptManager = promptManager;
    }

    public ChatResponse chat(ChatRequest request) {

        String systemPrompt = promptManager.load("system");

        String userPrompt =
                request.getMessages()
                        .getFirst()
                        .getContent();

        String answer =
                llmGateway.
                        generate(
                        systemPrompt,
                        userPrompt
                );

        ChatResponse response = new ChatResponse();
        response.setContent(answer);

        return response;
    }
}
