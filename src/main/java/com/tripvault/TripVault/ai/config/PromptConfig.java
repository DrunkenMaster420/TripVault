package com.tripvault.TripVault.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class PromptConfig {

    @Value("classpath:prompts/tripvault-system-prompt.txt")
    private Resource systemPrompt;

    public String getSystemPrompt(){
        try {
            return systemPrompt.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read system prompt.", e);
        }
    }
}
