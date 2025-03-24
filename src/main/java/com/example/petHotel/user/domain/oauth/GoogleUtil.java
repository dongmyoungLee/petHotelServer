package com.example.petHotel.user.domain.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleUtil {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String client;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String secret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUrl;
    private final RestTemplate restTemplate = new RestTemplate();


    public GoogleDTO requestToken(String accessCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", accessCode);
        params.add("client_id", client);
        params.add("client_secret", secret);
        params.add("redirect_uri", redirectUrl);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                entity,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        GoogleDTO googleToken = null;

        System.out.println(response.getBody());

        try {
            googleToken = objectMapper.readValue(response.getBody(), GoogleDTO.class);
        } catch (JsonProcessingException e) {
//            throw new E(ErrorStatus._PARSING_ERROR);
        }
        return googleToken;
    }

    public GoogleDTO.GoogleUserInfo socialLogin(String access_token) {
        String userResource = getUserResource(access_token);

        ObjectMapper objectMapper = new ObjectMapper();

        GoogleDTO.GoogleUserInfo googleUserInfo = null;


        try {
            googleUserInfo = objectMapper.readValue(userResource, GoogleDTO.GoogleUserInfo.class);
        } catch (JsonProcessingException e) {
//            throw new E(ErrorStatus._PARSING_ERROR);
        }
        return googleUserInfo;
    }

    private String getUserResource(String accessToken) {
        String resourceUri = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity entity = new HttpEntity(headers);

        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, String.class).getBody();
    }
}