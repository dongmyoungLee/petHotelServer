package com.example.petHotel.user.domain;


import com.example.petHotel.user.mock.TestClockHolder;
import com.example.petHotel.user.mock.TestPasswordEncryption;
import com.example.petHotel.user.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {
    @Test
    public void User는_UserCreate_객체로_생성할_수_있다() {
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
        User result = User.from(userCreate, new TestUuidHolder("aaaa-aaaaa-aaaaaaa-aaaaaaa"), new TestClockHolder(1679530673958L), new TestPasswordEncryption(new BCryptPasswordEncoder()));

        // then
        assertThat(result.getUserEmail()).isEqualTo("pajang1515@daum.net");
        assertThat(result.getUserName()).isEqualTo("dm");
        assertThat(result.getUserPhone()).isEqualTo("01012341234");
        assertThat(result.getUserAddr()).isEqualTo("경기도");
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(result.getCertificationCode()).isEqualTo("aaaa-aaaaa-aaaaaaa-aaaaaaa");
        assertThat(result.getUserRegistrationDate()).isEqualTo(1679530673958L);
        assertThat(new TestPasswordEncryption(new BCryptPasswordEncoder()).matches("1234", result.getUserPwd())).isTrue();
    }
}
