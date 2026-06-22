package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.repository.AllocationStrategy;
import com.tripvault.TripVault.repository.StorageAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageAllocationService {

    private final StorageAccountRepository storageAccountRepository;
    private final AllocationStrategy allocationStrategy;

    @Transactional
    public synchronized StorageAccount allocate(
            Long chunkSize
    ) {

        List<StorageAccount> accounts =
                storageAccountRepository.findByActiveTrue();

        StorageAccount account =
                allocationStrategy.selectAccount(
                        accounts,
                        chunkSize
                );

        account.setUsedQuota(
                account.getUsedQuota()
                        + chunkSize
        );

        storageAccountRepository.saveAndFlush(
                account
        );

        return account;
    }
}