package com.tripvault.TripVault.service;

import com.tripvault.TripVault.dto.StorageAccountResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface StorageAccountService {

    List<StorageAccountResponse> getMyStorageAccounts(Authentication authentication);

}
