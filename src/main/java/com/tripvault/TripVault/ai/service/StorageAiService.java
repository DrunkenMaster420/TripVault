package com.tripvault.TripVault.ai.service;

import com.tripvault.TripVault.ai.prompt.PromptManager;
import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.repository.StorageAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StorageAiService {

    private final StorageAccountRepository storageAccountRepository;
    private final PromptManager promptManager;
    private final LlmGateway llmGateway;

    public StorageAiService(StorageAccountRepository storageAccountRepository, PromptManager promptManager, LlmGateway llmGateway) {
        this.storageAccountRepository = storageAccountRepository;
        this.promptManager = promptManager;
        this.llmGateway = llmGateway;
    }

    public String analyze(Long storageId){
        StorageAccount storageAccount =
                storageAccountRepository.findById(storageId)
                        .orElseThrow(() ->
                                new RuntimeException("Storage account not found"));

        long remaining =
                storageAccount.getTotalQuota()
                        - storageAccount.getUsedQuota();

        Map<String, Object> variables = Map.of(
                "email", storageAccount.getGoogleEmail(),
                "usedGb", storageAccount.getUsedQuota(),
                "totalGb", storageAccount.getTotalQuota(),
                "remainingGb", remaining
        );

        String renderedPrompt =
                promptManager.render(
                        "storage-analysis",
                        variables
                );

        return llmGateway.generate(
                promptManager.load("system"),
                renderedPrompt
        );
    }
}
