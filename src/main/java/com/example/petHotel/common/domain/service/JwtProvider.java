package com.example.petHotel.common.domain.service;

import com.example.petHotel.common.domain.dto.TokenInfo;
import com.example.petHotel.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface JwtProvider {
    TokenInfo parseToken(String token);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String generateToken(User user, long expirationMillis);
    void addTokenToCookies(HttpServletResponse response, String accessToken, String refreshToken);
    void clearTokensCookie(HttpServletResponse response);
    void addAccessTokenToCookie(HttpServletResponse response, String accessToken);
    String getCookieValue(HttpServletRequest request, String cookieName);
    UUID getUserIdFromRefreshToken(String refreshToken);
    boolean validateToken(String token);
}
