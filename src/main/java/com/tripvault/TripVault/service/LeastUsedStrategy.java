package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.repository.AllocationStrategy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LeastUsedStrategy implements AllocationStrategy {
    @Override
    public StorageAccount selectAccount(
            List<StorageAccount> accounts,
            Long chunkSize
    ) {

        return accounts.stream()
                .filter(account ->
                        account.getTotalQuota() - account.getUsedQuota() >= chunkSize)
                .max(Comparator.comparing(
                        account -> account.getTotalQuota() - account.getUsedQuota()
                ))
                .orElseThrow(() ->
                        new RuntimeException("No storage account has enough free space."));
    }
}
