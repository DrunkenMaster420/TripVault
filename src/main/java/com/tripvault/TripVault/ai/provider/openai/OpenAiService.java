package com.tripvault.TripVault.ai.provider.openai;

import com.tripvault.TripVault.ai.config.PromptConfig;
import com.tripvault.TripVault.ai.dto.ChatRequest;
import com.tripvault.TripVault.ai.dto.ChatResponse;
import com.tripvault.TripVault.ai.service.LlmService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OpenAiService implements LlmService {

    private final ChatClient chatClient;
    private final PromptConfig promptConfig;

    public OpenAiService(ChatClient.Builder builder,
                         PromptConfig promptConfig) {
        this.chatClient = builder.build();
        this.promptConfig=promptConfig;
    }

    @Override
    public ChatResponse chat(ChatRequest request){

        String systemPrompt= promptConfig.getSystemPrompt();

        String prompt =
                request.getMessages()
                        .getFirst()
                        .getContent();

        String answer = chatClient
                .prompt()
                .system(systemPrompt)
                .user(prompt)
                .call()
                .content();

        ChatResponse response = new ChatResponse();
        response.setContent(answer);

        return response;
    }
}