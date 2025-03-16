package com.example.petHotel.user.controller;

import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.user.controller.request.UserLoginRequest;
import com.example.petHotel.user.controller.response.LoginResponse;
import com.example.petHotel.user.controller.response.TokenResponse;
import com.example.petHotel.user.domain.UserToken;
import com.example.petHotel.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {

        UserToken userToken = userService.login(userLoginRequest.getUserEmail(), userLoginRequest.getUserPwd());

        jwtProvider.addTokenToCookies(response, userToken.getAccessToken(), userToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(userToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtProvider.getCookieValue(request, "refresh_token");

        // refresh 검증 .. 만료 시 프론트에서 로그아웃 시킬 예정 ..
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            jwtProvider.clearTokensCookie(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(TokenResponse.builder().message("Invalid or expired refresh token").build());
        }

        String newAccessToken = userService.refreshAccessToken(refreshToken, response);

        jwtProvider.addAccessTokenToCookie(response, newAccessToken);

        return ResponseEntity.ok(TokenResponse.builder()
                .accessToken(newAccessToken)
                .message("Token refreshed successfully")
                .build());
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        jwtProvider.clearTokensCookie(response);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
