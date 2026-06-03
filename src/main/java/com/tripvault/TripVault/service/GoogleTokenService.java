package com.tripvault.TripVault.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripvault.TripVault.model.User;
import com.tripvault.TripVault.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleTokenService {

    private final UserRepository userRepository;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    private boolean isExpired(User user) {

        return user.getTokenExpiry() == null ||
                user.getTokenExpiry()
                        .isBefore(LocalDateTime.now().plusMinutes(5));
    }

    public String refreshAccessToken(User user) throws Exception {

        System.out.println(
                "Refreshing Google token for user: "
                        + user.getUsername()
        );

        String body =
                "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                        "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                        "&refresh_token=" + URLEncoder.encode(user.getRefreshToken(), StandardCharsets.UTF_8) +
                        "&grant_type=refresh_token";

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create("https://oauth2.googleapis.com/token"))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();

        HttpResponse<String> response =
                HttpClient.newHttpClient()
                        .send(request,
                                HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> tokenResponse =
                mapper.readValue(response.body(), Map.class);

        String newAccessToken =
                (String) tokenResponse.get("access_token");

        if (newAccessToken == null) {
            throw new RuntimeException(
                    "Failed to refresh token: " + response.body()
            );
        }

        Number expiresIn =
                (Number) tokenResponse.get("expires_in");

        user.setAccessToken(newAccessToken);

        user.setTokenExpiry(
                LocalDateTime.now()
                        .plusSeconds(expiresIn.longValue())
        );

        userRepository.save(user);

        return newAccessToken;
    }

    public String getValidAccessToken(User user) throws Exception {

        if (isExpired(user)) {
            return refreshAccessToken(user);
        }

        return user.getAccessToken();
    }
}
