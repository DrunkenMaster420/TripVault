package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.GoogleOAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final UserRepository userRepository;

    public GoogleAuthController(GoogleOAuthService googleOAuthService,
                                UserRepository userRepository) {
        this.googleOAuthService = googleOAuthService;
        this.userRepository = userRepository;
    }

    // Step 1: Redirect to Google
    @GetMapping("/login")
    public ResponseEntity<Void> redirectToGoogle(@RequestParam Long userId) {

        String url = googleOAuthService.getAuthorizationUrl(userId);

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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        return ResponseEntity.ok("Google Drive Connected Successfully!");
    }
}