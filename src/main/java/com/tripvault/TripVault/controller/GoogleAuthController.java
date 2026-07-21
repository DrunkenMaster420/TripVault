package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.dto.DriveStorageInfo;
import com.tripvault.TripVault.model.StorageAccount;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.StorageAccountRepository;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.GoogleDriveService;
import com.tripvault.TripVault.service.GoogleOAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final UserRepository userRepository;
    private final StorageAccountRepository storageAccountRepository;
    private final GoogleDriveService googleDriveService;

   @Value("${app.frontend.url:${FRONTEND_URL:http://localhost:5173}}")
private String frontendUrl;

    public GoogleAuthController(GoogleOAuthService googleOAuthService,
                                UserRepository userRepository,
                                StorageAccountRepository storageAccountRepository,
                                GoogleDriveService googleDriveService) {
        this.googleOAuthService = googleOAuthService;
        this.userRepository = userRepository;
        this.storageAccountRepository = storageAccountRepository;
        this.googleDriveService = googleDriveService;
    }

    // Step 1: Redirect to Google
    @GetMapping("/login")
    public ResponseEntity<String> redirectToGoogle(Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String url = googleOAuthService.getAuthorizationUrl(user.getId());

        return ResponseEntity.ok(url);
    }

    // Step 2: Handle callback
    @GetMapping("/callback")
    public ResponseEntity<Void> handleCallback(
            @RequestParam String code,
            @RequestParam String state) throws Exception {

        Long userId = Long.parseLong(state);

        Map<String, Object> tokens = googleOAuthService.exchangeCodeForTokens(code);

        String accessToken = (String) tokens.get("access_token");
        String refreshToken = (String) tokens.get("refresh_token");

        Map<String, Object> userInfo = googleOAuthService.getUserInfo(accessToken);
        String googleId = (String) userInfo.get("id");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Number expiresIn = (Number) tokens.get("expires_in");

        StorageAccount storageAccount = storageAccountRepository
                .findByOwnerAndGoogleId(user, googleId)
                .orElse(new StorageAccount());

        storageAccount.setOwner(user);
        storageAccount.setGoogleId(googleId);
        storageAccount.setAccessToken(accessToken);
        storageAccount.setRefreshToken(refreshToken);
        storageAccount.setTokenExpiry(
                LocalDateTime.now().plusSeconds(expiresIn.longValue())
        );
        storageAccount.setGoogleEmail((String) userInfo.get("email"));

        DriveStorageInfo storageInfo = googleDriveService.getStorageInfo(storageAccount);
        storageAccount.setTotalQuota(storageInfo.getTotalQuota());
        storageAccount.setUsedQuota(storageInfo.getUsedQuota());
        storageAccount.setActive(true);

        storageAccountRepository.save(storageAccount);

        System.out.println("RESOLVED FRONTEND URL: " + frontendUrl);

        // Dynamic redirect to frontend URL
        String redirectTarget = frontendUrl + "/storage-accounts";

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(redirectTarget))
                .build();
    }
}