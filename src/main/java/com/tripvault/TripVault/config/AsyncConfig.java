package com.tripvault.TripVault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean
    public ExecutorService uploadExecutor() {

        return Executors.newFixedThreadPool(5);

    }

}
