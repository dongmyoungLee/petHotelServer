package com.example.petHotel.user.service.auth;

import com.example.petHotel.common.domain.exception.OAuthException;
import com.example.petHotel.common.domain.exception.UsernameNotFoundException;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.domain.auth.UserToken;
import com.example.petHotel.user.domain.oauth.*;
import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserCreate;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.service.port.auth.PasswordEncryption;
import com.example.petHotel.user.service.port.auth.RefreshTokenRepository;
import com.example.petHotel.user.service.port.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Builder
@Getter
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;
    private final PasswordEncryption passwordEncryption;
    private final JwtProvider jwtProvider;
    private final KakaoUtil kakaoUtil;
    private final GoogleUtil googleUtil;
    private final NaverUtil naverUtil;

    public UserToken login(String email, String password) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(UsernameNotFoundException::new);

        if (!passwordEncryption.matches(password, user.getUserPwd())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        // Refresh Token을 DB에 저장 (이전 토큰 무효화 가능)
//        refreshTokenRepository.save(RefreshToken.builder().userId(user.getUserId()).token(refreshToken).build());

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
                .orElseThrow(UsernameNotFoundException::new);

        return jwtProvider.generateAccessToken(user);
    }

    public UserToken oAuthKaKaoLogin(String accessCode) throws JsonProcessingException {
        try {
            return oAuthLogin(accessCode, SnsType.KAKAO);
        } catch (Exception e) {
            throw new OAuthException("Kakao login failed: " + e.getMessage());
        }
    }

    public UserToken oAuthGoogleLogin(String accessCode) throws JsonProcessingException {
        try {
            return oAuthLogin(accessCode, SnsType.GOOGLE);
        } catch (Exception e) {
            throw new OAuthException("Google login failed: " + e.getMessage());
        }
    }

    public UserToken oAuthNaverLogin(String accessCode) throws JsonProcessingException {
        try {
            return oAuthLogin(accessCode, SnsType.NAVER);
        } catch (Exception e) {
            throw new OAuthException("Naver login failed: " + e.getMessage());
        }
    }

    /**
     * 공통 OAuth 로그인 처리 메서드
     */
    private UserToken oAuthLogin(String accessCode, SnsType snsType) throws JsonProcessingException {
        String email;
        String name;
        String phone;

        switch (snsType) {
            case KAKAO:
                KakaoDTO.OAuthToken kakaoToken = kakaoUtil.requestToken(accessCode);
                KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(kakaoToken);
                email = kakaoProfile.getKakao_account().getEmail();
                name = kakaoProfile.getProperties().getNickname();
                phone = null;
                break;

            case GOOGLE:
                GoogleDTO googleToken = googleUtil.requestToken(accessCode);
                GoogleDTO.GoogleUserInfo googleUserInfo = googleUtil.socialLogin(googleToken.getAccess_token());
                email = googleUserInfo.getEmail();
                name = googleUserInfo.getName();
                phone = null;
                break;

            case NAVER:
                NaverDTO naverToken = naverUtil.requestToken(accessCode);
                NaverDTO.NaverUserInfoResponse naverUserInfo = naverUtil.getUserInfo(naverToken.getAccess_token());
                email = naverUserInfo.getResponse().getEmail();
                name = naverUserInfo.getResponse().getName();
                phone = naverUserInfo.getResponse().getMobile();
                break;

            default:
                throw new IllegalArgumentException("지원되지 않는 SNS 타입 입니다.");
        }

        User user = userRepository.findByUserEmail(email)
                .orElseGet(() -> createNewUser(email, name, phone, snsType));

        return generateUserToken(user);
    }

    private UserToken generateUserToken(User user) {
        return UserToken.builder()
                .accessToken(jwtProvider.generateAccessToken(user))
                .refreshToken(jwtProvider.generateRefreshToken(user))
                .role(user.getRole())
                .id(user.getUserId())
                .name(user.getUserName())
                .email(user.getUserEmail())
                .build();
    }

    private User createNewUser(String email, String name, String phone, SnsType snsType) {
        UserCreate userCreate = UserCreate.builder()
                .userEmail(email)
                .userName(name)
                .userPwd("sns") // SNS 로그인 기본 비밀번호
                .status(UserStatus.ACTIVE)
                .userPhone(phone)
                .role(Role.CUSTOMER)
                .snsType(snsType)
                .build();

        User user = User.from(userCreate, uuidHolder, clockHolder, passwordEncryption);
        return userRepository.save(user);
    }

}
