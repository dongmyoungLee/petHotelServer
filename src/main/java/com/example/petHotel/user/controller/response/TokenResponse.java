package com.example.petHotel.user.controller.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class TokenResponse {
    private String accessToken;
    private String message;
    private HttpStatus statusCode;
}