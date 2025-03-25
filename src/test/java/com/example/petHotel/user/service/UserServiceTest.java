package com.example.petHotel.user.service;

import com.example.petHotel.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.petHotel.common.domain.exception.DuplicateDataException;
import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.mock.FakeJwtProvider;
import com.example.petHotel.user.domain.auth.UserToken;
import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserCreate;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.mock.*;
import com.example.petHotel.user.service.auth.CertificationService;
import com.example.petHotel.user.service.auth.AuthService;
import com.example.petHotel.user.service.user.UserService;
import jakarta.mail.MessagingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserServiceTest {
    private UserService userService;

    private AuthService authService;
    private FakeJwtProvider jwtProvider;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        FakeRefreshTokenRepository fakeRefreshTokenRepository = new FakeRefreshTokenRepository();
        TestPasswordEncryption passwordEncryption = new TestPasswordEncryption(new BCryptPasswordEncoder());
        jwtProvider = new FakeJwtProvider();

        this.userService = UserService.builder()
                .userRepository(fakeUserRepository)
                .refreshTokenRepository(fakeRefreshTokenRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new TestClockHolder(1678530673958L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .passwordEncryption(new TestPasswordEncryption(new BCryptPasswordEncoder()))
                .build();

        User save = fakeUserRepository.save(User.builder()
                .userId(UUID.fromString("ce670844-46bb-4f12-883a-810510bf5dae"))
                .userEmail("pajang1515@daum.net")
                .userPwd(passwordEncryption.encryptPassword("1234"))
                .userName("dm")
                .userPhone("01012341234")
                .userAddr("경기도")
                .role(Role.ADMIN)
                .status(UserStatus.PENDING)
                .userRegistrationDate(0L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build());


        fakeUserRepository.save(User.builder()
                .userId(UUID.fromString("ce670844-46bb-4f12-883a-810510bf5dac"))
                .userEmail("pajang1516@daum.net")
                .userPwd(passwordEncryption.encryptPassword("1234"))
                .userName("dm2")
                .userPhone("01034563456")
                .userAddr("서울")
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .userRegistrationDate(0L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build());

    }


    @Test
    public void UserCreate를_이용하여_유저를_생성할_수_있다() throws MessagingException {
        // given
        UserCreate userCreate = UserCreate.builder()
                .userEmail("pajang1515@daum.net")
                .userPwd("1234")
                .userName("dm")
                .userPhone("01012341234")
                .userAddr("경기도")
                .status(UserStatus.PENDING)
                .role(Role.CUSTOMER)
                .build();
        // when
        User result = userService.create(userCreate);

        // then
        assertThat(result.getUserEmail()).isEqualTo("pajang1515@daum.net");
        assertThat(result.getUserName()).isEqualTo("dm");
        assertThat(result.getUserPhone()).isEqualTo("01012341234");
        assertThat(result.getUserAddr()).isEqualTo("경기도");
        assertThat(result.getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(userService.getPasswordEncryption().matches("1234", result.getUserPwd())).isTrue();
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // given

        // when -> 로직을 수정해야할 필요가 있는 듯 함..

        userService.verifyEmail(UUID.fromString("ce670844-46bb-4f12-883a-810510bf5dae"), "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        User user = userService.getById(UUID.fromString("ce670844-46bb-4f12-883a-810510bf5dae"));

        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void Active_인_유저는_에러를_던진다() {
        // given

        // when


        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(UUID.fromString("ce670844-46bb-4f12-883a-810510bf5dac"), "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        }).isInstanceOf(DuplicateDataException.class);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        // given

        // when

        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(UUID.fromString("ce670844-46bb-4f12-883a-810510bf5dae"), "aaaaaaaa");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 존재하지_않는_ID로_유저를_조회하면_예외를_던진다() {
        // given
        UUID userId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> userService.getById(userId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 올바른_이메일과_비밀번호로_로그인할_수_있다() {
        // given
        String email = "pajang1515@daum.net";
        String password = "1234";

        // when
        UserToken userToken = authService.login(email, password);

        // then
        assertThat(userToken.getAccessToken()).isNotNull();
        assertThat(userToken.getRefreshToken()).isNotNull();
        assertThat(userToken.getEmail()).isEqualTo(email);
    }

    @Test
    void 존재하지_않는_이메일로_로그인하면_예외를_던진다() {
        // given
        String email = "nonexistent@daum.net";
        String password = "1234";

        // when & then
        assertThatThrownBy(() -> authService.login(email, password))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_예외를_던진다() {
        // given
        String email = "pajang1515@daum.net";
        String wrongPassword = "wrong_password";

        // when & then
        assertThatThrownBy(() -> authService.login(email, wrongPassword))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void 유효한_리프레시_토큰으로_새로운_엑세스_토큰을_발급할_수_있다() {
        // given
        User user = User.builder()
                .userId(UUID.fromString("ce670844-46bb-4f12-883a-810510bf5dac"))
                .userEmail("pajang1516@daum.net")
                .userPwd("1234")
                .userName("dm2")
                .userPhone("01034563456")
                .userAddr("서울")
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .userRegistrationDate(0L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .build();

        String refreshToken = jwtProvider.generateRefreshToken(user);

        // when
        String newAccessToken = authService.refreshAccessToken(refreshToken, null);

        // then
        assertThat(newAccessToken).isNotNull();
    }

    @Test
    void 잘못된_리프레시_토큰으로_엑세스_토큰_갱신하면_예외를_던진다() {
        // given
        String invalidRefreshToken = "invalid_token";

        // when & then
        assertThatThrownBy(() -> authService.refreshAccessToken(invalidRefreshToken, null))
                .isInstanceOf(UsernameNotFoundException.class);
    }

}
