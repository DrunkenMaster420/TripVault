package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.StorageAccountRepository;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.GoogleOAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final UserRepository userRepository;
    private final StorageAccountRepository storageAccountRepository;

    public GoogleAuthController(GoogleOAuthService googleOAuthService,
                                UserRepository userRepository, StorageAccountRepository storageAccountRepository) {
        this.googleOAuthService = googleOAuthService;
        this.userRepository = userRepository;
        this.storageAccountRepository=storageAccountRepository;
    }

    // Step 1: Redirect to Google
    @GetMapping("/login")
    public ResponseEntity<Void> redirectToGoogle(Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String url = googleOAuthService.getAuthorizationUrl(user.getId());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    // Step 2: Handle callback
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam String code,
            @RequestParam String state) throws Exception {

        Long userId = Long.parseLong(state);

        Map<String, Object> tokens =
                googleOAuthService.exchangeCodeForTokens(code);

        String accessToken = (String) tokens.get("access_token");
        String refreshToken = (String) tokens.get("refresh_token");

        Map<String, Object> userInfo =
                googleOAuthService.getUserInfo(accessToken);

        System.out.println("User Info From Google: " + userInfo);
        String googleId = (String) userInfo.get("id");

        System.out.println("Google ID: " + googleId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Number expiresIn =
                (Number) tokens.get("expires_in");

        StorageAccount storageAccount =
                new StorageAccount();

        storageAccount.setOwner(user);

        storageAccount.setAccessToken(accessToken);

        storageAccount.setRefreshToken(refreshToken);

        storageAccount.setTokenExpiry(
                LocalDateTime.now()
                        .plusSeconds(expiresIn.longValue())
        );

        storageAccount.setGoogleEmail(
                (String) userInfo.get("email")
        );

        storageAccount.setTotalQuota(
                15L * 1024 * 1024 * 1024
        );

        storageAccount.setUsedQuota(0L);

        storageAccount.setActive(true);

        storageAccountRepository.save(storageAccount);

        return ResponseEntity.ok("Google Drive Connected Successfully!");
    }


}