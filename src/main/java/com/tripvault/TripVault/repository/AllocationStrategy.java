package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.StorageAccount;

import java.util.List;

public interface AllocationStrategy {
    StorageAccount selectAccount(
            List<StorageAccount> accounts,
            Long chunkSize
    );
}
