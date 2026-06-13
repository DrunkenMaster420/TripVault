package com.tripvault.TripVault.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class GoogleOAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private static final String AUTH_URL =
            "https://accounts.google.com/o/oauth2/v2/auth";

    private static final String TOKEN_URL =
            "https://oauth2.googleapis.com/token";

    // 🔥 Now accepting state parameter
    public String getAuthorizationUrl(Long userId) {

        String scope = URLEncoder.encode(
                "https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
                StandardCharsets.UTF_8
        );

        return AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=" + scope +
                "&access_type=offline" +
                "&prompt=consent" +
                "&state=" + userId;
    }

    public Map<String, Object> exchangeCodeForTokens(String code)
            throws Exception {

        String body =
                "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                        "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                        "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                        "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                        "&grant_type=authorization_code";

        java.net.http.HttpRequest request =
                java.net.http.HttpRequest.newBuilder()
                        .uri(URI.create(TOKEN_URL))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                        .build();

        java.net.http.HttpResponse<String> response =
                HttpClient.newHttpClient()
                        .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        System.out.println("TOKEN RESPONSE BODY = " + response.body());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.body(), Map.class);
    }

    public Map<String, Object> getUserInfo(String accessToken) throws Exception {

        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(userInfoUrl))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        java.net.http.HttpResponse<String> response =
                HttpClient.newHttpClient()
                        .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), Map.class);
    }
}