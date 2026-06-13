package com.tripvault.TripVault.service;

import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.repository.AllocationStrategy;
import com.tripvault.TripVault.repository.StorageAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageAllocationService {

    private final StorageAccountRepository storageAccountRepository;
    private final AllocationStrategy allocationStrategy;

    public StorageAccount allocate(Long chunkSize){
        List<StorageAccount> accounts =
                storageAccountRepository.findByActiveTrue();

        return allocationStrategy.selectAccount(
                accounts,
                chunkSize
        );
    }

}
