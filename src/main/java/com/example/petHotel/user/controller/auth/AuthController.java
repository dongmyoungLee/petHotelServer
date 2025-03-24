package com.example.petHotel.user.controller.auth;

import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.user.controller.request.TokenValidRequest;
import com.example.petHotel.user.controller.request.UserLoginRequest;
import com.example.petHotel.user.controller.response.LoginResponse;
import com.example.petHotel.user.controller.response.TokenResponse;
import com.example.petHotel.user.domain.auth.UserToken;
import com.example.petHotel.user.service.user.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000, http://192.168.0.100:3000, http://192.168.0.101:3000, http://192.168.0.102:3000")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @GetMapping("/test")
    public ResponseEntity<?> test(@CookieValue(name = "access_token", required = false) String token) {
        return ResponseEntity.ok(Collections.singletonMap("message", "로그아웃 되었습니다."));
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {

        UserToken userToken = authService.login(userLoginRequest.getUserEmail(), userLoginRequest.getUserPwd());

        jwtProvider.addTokenToCookies(response, userToken.getAccessToken(), userToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(userToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(HttpServletResponse response, @RequestBody TokenValidRequest refreshToken) {
        // refresh 검증 .. 만료 시 프론트에서 로그아웃 시킬 예정 ..
        if (refreshToken.getToken() == null || !jwtProvider.validateToken(refreshToken.getToken())) {
            jwtProvider.clearTokensCookie(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(TokenResponse.builder()
                    .message("Invalid or expired refresh token")
                    .statusCode(HttpStatus.UNAUTHORIZED)
                    .build());
        }

        String newAccessToken = authService.refreshAccessToken(refreshToken.getToken(), response);

        jwtProvider.addAccessTokenToCookie(response, newAccessToken);

        return ResponseEntity.ok(TokenResponse.builder()
                .accessToken(newAccessToken)
                .statusCode(HttpStatus.CREATED)
                .message("Token refreshed successfully")
                .build());
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        jwtProvider.clearTokensCookie(response);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse response) throws JsonProcessingException {
        return handleSocialLogin(authService.oAuthKaKaoLogin(accessCode), response);
    }

    @GetMapping("/google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestParam("code") String accessCode, HttpServletResponse response) throws JsonProcessingException {
        return handleSocialLogin(authService.oAuthGoogleLogin(accessCode), response);
    }

    @GetMapping("/naver")
    public ResponseEntity<LoginResponse> naverLogin(@RequestParam("code") String accessCode, HttpServletResponse response) throws JsonProcessingException {
        return handleSocialLogin(authService.oAuthNaverLogin(accessCode), response);
    }

    private ResponseEntity<LoginResponse> handleSocialLogin(UserToken userToken, HttpServletResponse response) {
        return createLoginResponse(userToken, response);
    }

    private ResponseEntity<LoginResponse> createLoginResponse(UserToken userToken, HttpServletResponse response) {
        jwtProvider.addTokenToCookies(response, userToken.getAccessToken(), userToken.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(userToken));
    }
}
