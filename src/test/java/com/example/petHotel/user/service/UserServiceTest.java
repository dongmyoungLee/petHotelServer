package com.example.petHotel.user.service;

import com.example.petHotel.user.domain.Role;
import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserCreate;
import com.example.petHotel.user.domain.UserStatus;
import com.example.petHotel.user.mock.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        TestPasswordEncryption passwordEncryption = new TestPasswordEncryption(new BCryptPasswordEncoder());

        this.userService = UserService.builder()
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new TestClockHolder(1678530673958L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .passwordEncryption(new TestPasswordEncryption(new BCryptPasswordEncoder()))
                .build();

        fakeUserRepository.save(User.builder()
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
    public void UserCreate를_이용하여_유저를_생성할_수_있다() {
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

        // when
//        userService.verifyEmail("TEST", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
//
//        // then
//        User user = userServiceImpl.getById(1);
//
//        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}
