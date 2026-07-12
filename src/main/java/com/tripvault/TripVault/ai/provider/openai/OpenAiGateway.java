package com.tripvault.TripVault.ai.provider.openai;

import com.tripvault.TripVault.ai.service.LlmGateway;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAiGateway implements LlmGateway {

    private final ChatClient chatClient;

    public OpenAiGateway(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String generate(
            String systemPrompt,
            String userPrompt
    ) {

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }
}