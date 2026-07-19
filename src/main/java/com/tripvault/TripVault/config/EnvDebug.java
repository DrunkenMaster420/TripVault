package com.tripvault.TripVault.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvDebug {

    @Bean
    CommandLineRunner test() {
        return args -> {
            System.out.println("DB_URL = " + System.getenv("DB_URL"));
            System.out.println("spring.datasource.url = " + System.getProperty("spring.datasource.url"));
        };
    }
}
