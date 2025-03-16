package com.example.petHotel.user.service;

import com.example.petHotel.common.domain.exception.DuplicateDataException;
import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.controller.response.LoginResponse;
import com.example.petHotel.user.domain.*;
import com.example.petHotel.user.service.port.PasswordEncryption;
import com.example.petHotel.user.service.port.RefreshTokenRepository;
import com.example.petHotel.user.service.port.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Builder
@Getter
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;
    private final PasswordEncryption passwordEncryption;
    private final CertificationService certificationService;
    private final JwtProvider jwtProvider;


    public User getById(UUID userId) {
        return userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Users", userId));
    }

    @Transactional
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate, uuidHolder, clockHolder, passwordEncryption);
        user = userRepository.save(user);
        certificationService.send(userCreate.getUserEmail(), user.getUserId(), user.getCertificationCode());

        return user;
    }
    @Transactional
    public void verifyEmail(UUID userId, String certificationCode) {

        userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE).ifPresent(user -> {
                    throw new DuplicateDataException();
            });

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("가입된 유저가 없습니다.", userId));

        user = user.certificate(certificationCode);

        userRepository.save(user);
    }

    public UserToken login(String email, String password) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncryption.matches(password, user.getUserPwd())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        // Refresh Token을 DB에 저장 (이전 토큰 무효화 가능)
        refreshTokenRepository.save(RefreshToken.builder().userId(user.getUserId()).token(refreshToken).build());

        return UserToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .id(user.getUserId())
                .name(user.getUserName())
                .email(user.getUserEmail())
                .build();
    }

    public String refreshAccessToken(String refreshToken, HttpServletResponse response) {
        UUID userId = jwtProvider.getUserIdFromRefreshToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return jwtProvider.generateAccessToken(user);
    }

//    public TokenResponse refreshAccessToken(String refreshToken) {
//        Long userId = jwtProvider.validateRefreshToken(refreshToken);
//        RefreshToken storedToken = refreshTokenRepository.findByUserId(userId)
//                .orElseThrow(() -> new UnauthorizedException("Refresh Token이 존재하지 않습니다."));
//
//        if (!storedToken.getToken().equals(refreshToken)) {
//            throw new UnauthorizedException("유효하지 않은 Refresh Token입니다.");
//        }
//
//        String newAccessToken = jwtProvider.generateAccessToken(userId);
//        return new TokenResponse(newAccessToken);
//    }


}
