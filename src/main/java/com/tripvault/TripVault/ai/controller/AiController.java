package com.tripvault.TripVault.ai.controller;

import com.tripvault.TripVault.ai.dto.ChatRequest;
import com.tripvault.TripVault.ai.dto.ChatResponse;
import com.tripvault.TripVault.ai.service.LlmService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final LlmService llmService;

    public AiController(LlmService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        System.out.println("AI Controller Called");
        return llmService.chat(request);
    }
}
