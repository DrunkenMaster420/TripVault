package com.tripvault.TripVault.service;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

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

    public String getAuthorizationUrl() {
        return AUTH_URL +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=https://www.googleapis.com/auth/drive.file" +
                "&access_type=offline" +
                "&prompt=consent" ;
    }

    public Map<String, Object> exchangeCodeForTokens(String code)
            throws Exception {

        String body = "code=" + code +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code";

        HttpRequestFactory requestFactory =
                new NetHttpTransport().createRequestFactory();

        HttpContent content =
                new ByteArrayContent(
                        "application/x-www-form-urlencoded",
                        body.getBytes());

        HttpRequest request =
                requestFactory.buildPostRequest(
                        new GenericUrl(TOKEN_URL),
                        content);

        HttpResponse response = request.execute();

        return new ObjectMapper()
                .readValue(response.getContent(), Map.class);
    }



}
