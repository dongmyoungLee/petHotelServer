package com.example.petHotel.user.domain.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class NaverUtil {
    @Value("${spring.naver.client-id}")
    private String client;
    @Value("${spring.naver.client-secret}")
    private String secret;
    @Value("${spring.naver.redirect-uri}")
    private String redirectUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    public NaverDTO requestToken(String accessCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", accessCode);
        params.add("client_id", client);
        params.add("client_secret", secret);
        params.add("redirect_uri", redirectUrl);
        params.add("grant_type", "authorization_code");
        params.add("state", "STATE_STRING");

        HttpHeaders headers = new HttpHeaders();

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                entity,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        NaverDTO naverToken = null;


        try {
            naverToken = objectMapper.readValue(response.getBody(), NaverDTO.class);
        } catch (JsonProcessingException e) {

        }
        return naverToken;
    }

    public NaverDTO.NaverUserInfoResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                entity,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        NaverDTO.NaverUserInfoResponse naverUserInfoResponse = null;


        try {
            naverUserInfoResponse = objectMapper.readValue(response.getBody(), NaverDTO.NaverUserInfoResponse.class);
        } catch (JsonProcessingException e) {

        }
        return naverUserInfoResponse;

    }
}
