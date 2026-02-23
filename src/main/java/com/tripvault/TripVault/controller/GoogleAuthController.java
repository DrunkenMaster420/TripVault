package com.tripvault.TripVault.controller;

import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import com.tripvault.TripVault.service.GoogleOAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final GoogleOAuthService googleOAuthService;

    private final UserRepository userRepository;

    public GoogleAuthController(
            GoogleOAuthService googleOAuthService,
            UserRepository userRepository) {
        this.googleOAuthService = googleOAuthService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> redirectToGoogle() {
        String url = googleOAuthService.getAuthorizationUrl();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam String code,
            @RequestParam Long userId) throws Exception {

        Map<String, Object> tokens =
                googleOAuthService.exchangeCodeForTokens(code);

        String accessToken =
                (String) tokens.get("access_token");

        String refreshToken =
                (String) tokens.get("refresh_token");

        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        return ResponseEntity.ok("Google Drive Connected!");
    }

}
