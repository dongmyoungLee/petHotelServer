package com.example.petHotel.common.infrastructure;

import com.example.petHotel.common.domain.dto.TokenInfo;
import com.example.petHotel.common.domain.exception.UnauthorizedException;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.common.infrastructure.jwt.JwtProviderImpl;
import com.example.petHotel.user.domain.Role;
import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserStatus;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class JwtProviderTest {
    private JwtProvider jwtProvider;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProviderImpl();

        ReflectionTestUtils.setField(jwtProvider, "secret", "mySuperSecretKeyThatIsLongEnoughForHMAC");

        // 테스트용 사용자 생성
        testUser = User.builder()
                .userId(UUID.randomUUID()) // UUID 생성
                .userEmail("pajang1515@daum.net")
                .userPwd("1234")
                .userName("dm")
                .userPhone("01055401958")
                .userAddr("seoul")
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .userRegistrationDate(System.currentTimeMillis())
                .certificationCode("test")
                .build();
    }

    @Test
    public void User로_AccessToken_을_발급_받을_수_있다() {
        // given

        // when
        String token = jwtProvider.generateAccessToken(testUser);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void AccessToken_을_파싱하여_사용자_정보를_얻을_수_있다() {
        // given
        String token = jwtProvider.generateAccessToken(testUser);

        // when
        TokenInfo tokenInfo = jwtProvider.parseToken(token);

        // then
        assertThat(tokenInfo).isNotNull();
        assertThat(tokenInfo.getId()).isEqualTo(testUser.getUserId());
        assertThat(tokenInfo.getEmail()).isEqualTo(testUser.getUserEmail());
        assertThat(tokenInfo.getRole()).isEqualTo(testUser.getRole().toString());
    }

    @Test
    void 잘못된_토큰을_파싱하면_예외가_발생해야한다() {
        // given
        String invalidToken = "invalid.token.here";

        // when & then
        assertThatThrownBy(() -> jwtProvider.parseToken(invalidToken))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 만료된_토큰을_검증하면_예외가_발생해야한다() {
        // given
        String expiredToken = jwtProvider.generateToken(testUser, 1);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // when & then
        assertThatThrownBy(() -> jwtProvider.parseToken(expiredToken))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 유효한_토큰은_validateToken_검사에서_true_를_반환해야한다() {
        // given
        String token = jwtProvider.generateAccessToken(testUser);

        // when
        boolean isValid = jwtProvider.validateToken(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    void 잘못된_토큰은_validateToken_검사에서_false_를_반환해야한다() {
        // given
        String invalidToken = "invalid.token.value";

        // when
        boolean isValid = jwtProvider.validateToken(invalidToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    void 리프레시토큰에서_사용자_ID를_추출할_수_있다() {
        // given
        String refreshToken = jwtProvider.generateRefreshToken(testUser);

        // when
        UUID extractedUserId = jwtProvider.getUserIdFromRefreshToken(refreshToken);

        // then
        assertThat(extractedUserId).isEqualTo(testUser.getUserId());
    }

    @Test
    void 로그아웃_시_토큰_쿠키를_제대로_삭제해야한다() {
        // given
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // when
        jwtProvider.clearTokensCookie(response);

        // then
        Mockito.verify(response, Mockito.times(2)).addCookie(Mockito.argThat(cookie ->
                (cookie.getName().equals("access_token") || cookie.getName().equals("refresh_token"))
                        && cookie.getMaxAge() == 0
                        && cookie.getValue() == null
        ));
    }

    @Test
    void addTokenToCookies_토큰을_쿠키에_정상적으로_추가해야한다() {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String accessToken = "testAccessToken";
        String refreshToken = "testRefreshToken";

        // when
        jwtProvider.addTokenToCookies(response, accessToken, refreshToken);

        // then
        assertThat(response.getCookies())
                .extracting(Cookie::getName, Cookie::getValue)
                .containsExactlyInAnyOrder(
                        tuple("access_token", accessToken),
                        tuple("refresh_token", refreshToken)
                );
    }

    @Test
    void addAccessTokenToCookie_엑세스_토큰을_쿠키에_추가해야한다() {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        String accessToken = "testAccessToken";

        // when
        jwtProvider.addAccessTokenToCookie(response, accessToken);

        // then
        assertThat(response.getCookies())
                .extracting(Cookie::getName, Cookie::getValue)
                .containsExactly(tuple("access_token", accessToken));
    }

    @Test
    void getCookieValue_올바른_쿠키값을_반환해야한다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(
                new Cookie("access_token", "testAccessToken"),
                new Cookie("refresh_token", "testRefreshToken")
        );

        // when
        String accessToken = jwtProvider.getCookieValue(request, "access_token");
        String refreshToken = jwtProvider.getCookieValue(request, "refresh_token");
        String invalidToken = jwtProvider.getCookieValue(request, "invalid_token");

        // then
        assertThat(accessToken).isEqualTo("testAccessToken");
        assertThat(refreshToken).isEqualTo("testRefreshToken");
        assertThat(invalidToken).isNull();
    }
}
