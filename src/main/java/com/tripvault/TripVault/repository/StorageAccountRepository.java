package com.tripvault.TripVault.repository;

import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageAccountRepository extends JpaRepository<StorageAccount, Long> {

    List<StorageAccount> findByActiveTrue();

    List<StorageAccount> findByOwner(User owner);

}
