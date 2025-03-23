package com.example.petHotel.user.service;

import com.example.petHotel.common.domain.exception.DuplicateDataException;
import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.domain.exception.UsernameNotFoundException;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.domain.*;
import com.example.petHotel.user.service.port.PasswordEncryption;
import com.example.petHotel.user.service.port.RefreshTokenRepository;
import com.example.petHotel.user.service.port.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;
    private final PasswordEncryption passwordEncryption;
    private final CertificationService certificationService;
    private final JwtProvider jwtProvider;
    private final KakaoUtil kakaoUtil;
    private final GoogleUtil googleUtil;


    public User getById(UUID userId) {
        return userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Users", userId));
    }

    @Transactional
    public User create(UserCreate userCreate) throws MessagingException {
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
                .orElseThrow(UsernameNotFoundException::new);

        System.out.println(user);

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
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        String email = kakaoProfile.getKakao_account().getEmail();

        User user = userRepository.findByUserEmail(email).orElseGet(() -> createNewUser(kakaoProfile, "KAKAO"));

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return UserToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .id(user.getUserId())
                .name(user.getUserName())
                .email(user.getUserEmail())
                .build();
    }

    private User createNewUser(Object snsProfile, String snsType) {
        UserCreate userCreate;

        switch (snsType) {
            case "KAKAO":
                KakaoDTO.KakaoProfile kakaoProfile = (KakaoDTO.KakaoProfile) snsProfile;
                userCreate = UserCreate.builder()
                        .userEmail(kakaoProfile.getKakao_account().getEmail())
                        .userName(kakaoProfile.getProperties().getNickname())
                        .userPwd("sns") // SNS 로그인용 기본 비밀번호
                        .status(UserStatus.ACTIVE)
                        .role(Role.CUSTOMER)
                        .snsType(SnsType.KAKAO)
                        .build();
                break;

            case "GOOGLE":
                GoogleDTO.GoogleUserInfo googleUserInfo = (GoogleDTO.GoogleUserInfo) snsProfile;
                userCreate = UserCreate.builder()
                        .userEmail(googleUserInfo.getEmail())
                        .userName("구글이름 가져와야함")
                        .userPwd("sns")
                        .status(UserStatus.ACTIVE)
                        .role(Role.CUSTOMER)
                        .snsType(SnsType.GOOGLE)
                        .build();
                break;

            default:
                throw new IllegalArgumentException("지원되지 않는 SNS 타입입니다.");
        }

        User user = User.from(userCreate, uuidHolder, clockHolder, passwordEncryption);
        return userRepository.save(user);
    }


    public UserToken oAuthGoogleLogin(String accessCode) {
        GoogleDTO token = googleUtil.requestToken(accessCode);
        GoogleDTO.GoogleUserInfo googleUserInfo = googleUtil.socialLogin(token.getAccess_token());

        User user = userRepository.findByUserEmail(googleUserInfo.getEmail()).orElseGet(() -> createNewUser(googleUserInfo, "GOOGLE"));

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return UserToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .id(user.getUserId())
                .name(user.getUserName())
                .email(user.getUserEmail())
                .build();
    }
}
