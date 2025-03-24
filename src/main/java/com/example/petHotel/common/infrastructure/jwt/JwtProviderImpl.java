package com.example.petHotel.common.infrastructure.jwt;

import com.example.petHotel.common.domain.dto.TokenInfo;
import com.example.petHotel.common.domain.exception.UnauthorizedException;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.user.domain.user.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@Service
public class JwtProviderImpl implements JwtProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateAccessToken(User user) {
        return generateToken(user, 1000L * 60 * 30); // 30분 유효
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateToken(user, 1000L * 60 * 60 * 24 * 7); // 7일 유효
    }

    @Override
    public String generateToken(User user, long expirationMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId().toString());
        claims.put("name", user.getUserName());
        claims.put("email", user.getUserEmail());
        claims.put("role", user.getRole().toString());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + expirationMillis);

        claims.put("iat", nowMillis / 1000); // 발급 시간 (초 단위)
        claims.put("exp", expiration.getTime() / 1000); // 만료 시간 (초 단위)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUserId().toString())
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiration) // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    /**
     * JWT 토큰을 파싱하여 사용자 정보를 추출하는 메서드
     * @param token - 클라이언트가 보낸 JWT
     * @return TokenInfo - JWT에서 추출한 사용자 정보
     */
    @Override
    public TokenInfo parseToken(String token) {
        try {
            Claims body = (Claims) Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parse(token)
                    .getBody();

            return TokenInfo.builder()
                    .id(UUID.fromString(body.get("id", String.class)))
                    .name(body.get("name", String.class))
                    .email(body.get("email", String.class))
                    .role(body.get("role", String.class))
                    .build();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            throw new UnauthorizedException("The access token has expired. Please refresh your token.");
        } catch (JwtException | IllegalArgumentException e) {
            // 잘못된 토큰
            throw new UnauthorizedException("Invalid token. Please check your access token.");
        }

    }

    @Override
    public void addTokenToCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        // Access Token을 쿠키에 설정 (HttpOnly, Secure, SameSite)
        Cookie accessCookie = new Cookie("access_token", accessToken);

        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(1000 * 60 * 30);
        accessCookie.setDomain("localhost");
        accessCookie.setAttribute("SameSite", "Lax");


        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);

        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(1000 * 60 * 60 * 24 * 7);
        refreshCookie.setDomain("localhost");
        refreshCookie.setAttribute("SameSite", "Lax");



        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
//
//
//        // 모바일테스트
//
//        Cookie accessCookieIp = new Cookie("access_token", accessToken);
//        accessCookieIp.setHttpOnly(true);
//        accessCookieIp.setSecure(false);
//        accessCookieIp.setPath("/");
//        accessCookieIp.setMaxAge(1000 * 60 * 30);
//        accessCookieIp.setDomain("192.168.0.100"); // 192.168.0.100 도메인에 대한 쿠키
//        accessCookieIp.setAttribute("SameSite", "Lax");
//
//        Cookie refreshCookieIp = new Cookie("refresh_token", refreshToken);
//        refreshCookieIp.setHttpOnly(true);
//        refreshCookieIp.setSecure(false);
//        refreshCookieIp.setPath("/");
//        refreshCookieIp.setMaxAge(1000 * 60 * 60 * 24 * 7);
//        refreshCookieIp.setDomain("192.168.0.100"); // 192.168.0.100 도메인에 대한 쿠키
//        refreshCookieIp.setAttribute("SameSite", "Lax");
//
    }

    @Override
    public void clearTokensCookie(HttpServletResponse response) {
        // 리프레시 토큰 삭제
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        // 엑세스 토큰 삭제
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);
    }

    @Override
    public void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 30);
        accessTokenCookie.setDomain("localhost");
        accessTokenCookie.setAttribute("SameSite", "Lax");
        response.addCookie(accessTokenCookie);
    }

    @Override
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public UUID getUserIdFromRefreshToken(String refreshToken) {
        try {
            // JWT 토큰에서 클레임 파싱
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // 클레임에서 userId 추출하고 UUID로 변환
            String userId = claims.get("id", String.class);
            return UUID.fromString(userId);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid or expired JWT token", e);
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
