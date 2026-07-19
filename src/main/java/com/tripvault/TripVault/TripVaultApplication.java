package com.tripvault.TripVault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TripVaultApplication {

	public static void main(String[] args) {
		System.out.println("DB_URL env = " + System.getenv("DB_URL"));
		SpringApplication.run(TripVaultApplication.class, args);
		System.out.println("DB_URL env = " + System.getenv("DB_URL"));

	}

}
