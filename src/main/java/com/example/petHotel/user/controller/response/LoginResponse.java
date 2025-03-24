package com.example.petHotel.user.controller.response;

import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.auth.UserToken;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Role role;
    private UUID id;
    private String name;
    private String email;

    public static LoginResponse from(UserToken token) {
        return LoginResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .role(token.getRole())
                .id(token.getId())
                .name(token.getName())
                .email(token.getEmail())
                .build();
    }



}