package com.example.petHotel.common.mock;

import com.example.petHotel.common.domain.dto.TokenInfo;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeJwtProvider implements JwtProvider {
    private final Map<String, UUID> tokenStorage = new HashMap<>();

    @Override
    public String generateAccessToken(User user) {
        String accessToken = "fake-access-token-for-" + user.getUserId();
        tokenStorage.put(accessToken, user.getUserId());
        return accessToken;
    }

    @Override
    public String generateRefreshToken(User user) {
        String refreshToken = "fake-refresh-token-for-" + user.getUserId();
        tokenStorage.put(refreshToken, user.getUserId());
        return refreshToken;
    }

    @Override
    public String generateToken(User user, long expirationMillis) {
        String token = "fake-token-for-" + user.getUserId();
        tokenStorage.put(token, user.getUserId());
        return token;
    }

    @Override
    public TokenInfo parseToken(String token) {
        UUID userId = tokenStorage.get(token);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        return TokenInfo.builder()
                .id(userId)
                .name("fake-name-for-" + userId)
                .email("fake-email-for-" + userId + "@example.com")
                .role("USER")
                .build();
    }

    @Override
    public void addTokenToCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        // 테스트에서는 쿠키 저장을 시뮬레이션하지 않음
    }

    @Override
    public void clearTokensCookie(HttpServletResponse response) {
        // 테스트에서는 쿠키 제거를 시뮬레이션하지 않음
    }

    @Override
    public void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        // 테스트에서는 쿠키 저장을 시뮬레이션하지 않음
    }

    @Override
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        return null; // 테스트에서는 쿠키 값을 사용하지 않음
    }

    @Override
    public UUID getUserIdFromRefreshToken(String refreshToken) {
        return tokenStorage.getOrDefault(refreshToken, null);
    }

    @Override
    public boolean validateToken(String token) {
        return tokenStorage.containsKey(token);
    }
}