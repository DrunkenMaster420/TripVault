package com.tripvault.TripVault.ai.prompt;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class PromptManager {

    private final ResourceLoader resourceLoader;

    public PromptManager(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String load(String promptName) {

        Resource resource = resourceLoader.getResource(
                "classpath:prompts/" + promptName + ".st"
        );

        try {
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load prompt: " + promptName,
                    e
            );
        }
    }
    public String render(
            String promptName,
            Map<String, Object> variables
    ) {

        String template = load(promptName);

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            template = template.replace(
                    "{" + entry.getKey() + "}",
                    String.valueOf(entry.getValue())
            );
        }

        return template;
    }

}
