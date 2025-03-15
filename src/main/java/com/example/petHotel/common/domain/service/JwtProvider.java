package com.example.petHotel.common.domain.service;

import com.example.petHotel.common.domain.dto.TokenInfo;
import com.example.petHotel.user.domain.User;

import java.util.Map;

public interface JwtProvider {
    String generateToken(User user, String flag);

    TokenInfo parseToken(String token);

    boolean isTokenExpired(String token);

    String generateNewAccessToken(Map<String, Object> claims);
}
