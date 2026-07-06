package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.dto.StorageAccountResponse;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.StorageAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/storage-accounts")
public class StorageAccountController {
    private final StorageAccountService storageAccountService;

    public StorageAccountController(StorageAccountService storageAccountService, UserRepository userRepository) {
        this.storageAccountService = storageAccountService;
    }

    @GetMapping
    public List<StorageAccountResponse> getAccounts(Authentication authentication) {

       return storageAccountService.getMyStorageAccounts(authentication);

    }


}
