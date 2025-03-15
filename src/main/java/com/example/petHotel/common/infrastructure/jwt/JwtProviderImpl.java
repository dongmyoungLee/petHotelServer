package com.example.petHotel.common.infrastructure.jwt;

import com.example.petHotel.common.domain.dto.TokenInfo;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtProviderImpl implements JwtProvider {
    @Value("${jwt.secret}")
    private String secret;


    /**
     * JWT 토큰 생성 메서드
     * @param user - 토큰에 포함할 사용자 정보
     * @param flag - "access"(액세스 토큰) 또는 "refresh"(리프레시 토큰) 구분 값
     * @return 생성된 JWT 토큰 문자열
     */
    @Override
    public String generateToken(User user, String flag) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId().toString());
        claims.put("name", user.getUserName());
        claims.put("email", user.getUserEmail());
        claims.put("role", user.getRole().toString());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Date expiration;
        if (flag.equals("access")) {
            expiration = new Date(nowMillis + (1000L * 60 * 30)); // 액세스 토큰(30분 유효)
        } else {
            expiration = new Date(nowMillis + (1000L * 60 * 60 * 24 * 7)); // 리프레시 토큰(7일 유효)
        }

        claims.put("iat", nowMillis / 1000); // 발급 시간 (초 단위)
        claims.put("exp", expiration.getTime() / 1000); // 만료 시간 (초 단위)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUserId().toString())
                .setExpiration(
                        flag.equals("access") ?
                                new Date(System.currentTimeMillis() + (1000L * 60 * 30)) : // 액세스 토큰(30분 유효)
                                new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 7))) // 리프레시 토큰(7일 유효)
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
    }

    /**
     * JWT 토큰의 만료 여부를 확인하는 메서드
     * @param token - 검증할 JWT
     * @return true: 만료됨 / false: 유효함
     */
    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaims(token);
            Date expirationDate = claims.getExpiration();

            return expirationDate.before(new Date());
        } catch (Exception e) {
            // If there's an exception, the token is considered expired
            return true;
        }
    }

    /**
     * 새로운 액세스 토큰을 생성하는 메서드
     * @param claims - 기존 토큰에서 추출한 사용자 정보
     * @return 새롭게 생성된 액세스 토큰
     */
    @Override
    public String generateNewAccessToken(Map<String, Object> claims) {
        SecretKeySpec key = getSecretKeySpec();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 1800_000)) // 30분
                .signWith(key)
                .compact();
    }

    // 시크릿 키를 생성하는 메서드 (HMAC-SHA384 알고리즘 사용)
    private SecretKeySpec getSecretKeySpec() {
        SignatureAlgorithm hs384 = SignatureAlgorithm.HS384;
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), hs384.getJcaName());
        return key;
    }


    /**
     * JWT에서 클레임(Claims) 정보를 가져오는 메서드
     * @param token - JWT
     * @return Claims - JWT의 데이터(페이로드)
     */
    private Claims getClaims(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
