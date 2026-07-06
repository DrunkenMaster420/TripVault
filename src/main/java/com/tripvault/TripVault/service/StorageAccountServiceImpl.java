package com.tripvault.TripVault.service;

import com.tripvault.TripVault.dto.StorageAccountResponse;
import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.StorageAccountRepository;
import com.tripvault.TripVault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class StorageAccountServiceImpl implements StorageAccountService {

    private final StorageAccountRepository storageAccountRepository;
    private final UserRepository userRepository;


    @Override
    public List<StorageAccountResponse> getMyStorageAccounts(Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<StorageAccountResponse> responses = new ArrayList<>();
        List<StorageAccount> accounts = storageAccountRepository.findByOwner(user);

        for(StorageAccount account:accounts) {
            StorageAccountResponse response = new StorageAccountResponse();
            response.setId(account.getId());
            response.setGoogleEmail(account.getGoogleEmail());
            response.setUsedQuota(account.getUsedQuota());
            response.setTotalQuota(account.getTotalQuota());
            response.setActive(account.getActive());
           responses.add(response);
        }
        return responses;
    }
}
