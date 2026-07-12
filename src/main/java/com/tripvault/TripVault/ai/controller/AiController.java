package com.tripvault.TripVault.ai.controller;

import com.tripvault.TripVault.ai.dto.ChatRequest;
import com.tripvault.TripVault.ai.dto.ChatResponse;
import com.tripvault.TripVault.ai.service.AiService;
import com.tripvault.TripVault.ai.service.StorageAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private final StorageAiService storageAiService;

    public AiController(
            AiService aiService,
            StorageAiService storageAiService) {

        this.aiService = aiService;
        this.storageAiService = storageAiService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return aiService.chat(request);
    }

    @GetMapping("/storage/{id}/analyze")
    public String analyzeStorage(@PathVariable Long id) {
        return storageAiService.analyze(id);
    }
}
