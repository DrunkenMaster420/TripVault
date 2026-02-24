package com.tripvault.TripVault.service;

import tools.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
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

        return AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/drive.file" +
                "&access_type=offline" +
                "&prompt=consent" +
                "&state=" + userId;
    }

    public Map<String, Object> exchangeCodeForTokens(String code)
            throws Exception {

        String body = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&grant_type=authorization_code";

        HttpRequestFactory requestFactory =
                new NetHttpTransport().createRequestFactory();

        HttpContent content =
                new ByteArrayContent(
                        "application/x-www-form-urlencoded",
                        body.getBytes(StandardCharsets.UTF_8));

        HttpRequest request =
                requestFactory.buildPostRequest(
                        new GenericUrl(TOKEN_URL),
                        content);

        HttpResponse response = request.execute();

        return new ObjectMapper()
                .readValue(response.getContent(), Map.class);
    }
}