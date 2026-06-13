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
                .filter(a ->
                        a.getTotalQuota()
                                - a.getUsedQuota()
                                >= chunkSize)
                .min(Comparator.comparing(
                        StorageAccount::getUsedQuota))
                .orElseThrow();
    }
}
