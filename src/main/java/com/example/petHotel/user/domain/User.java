package com.example.petHotel.user.domain;

import com.example.petHotel.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class User {
    private UUID userId;
    private String userEmail;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddr;
    private UserStatus status;
    private Role role;
    private long userRegistrationDate;
    private String certificationCode;

    public static User from(UserCreate userCreate, UuidHolder uuidHolder, ClockHolder clockHolder) {
        return User.builder()
                .userEmail(userCreate.getUserEmail())
                .userPwd(userCreate.getUserPwd())
                .userName(userCreate.getUserName())
                .userPhone(userCreate.getUserPhone())
                .userAddr(userCreate.getUserAddr())
                .role(userCreate.getRole())
                .status(userCreate.getStatus())
                .userRegistrationDate(clockHolder.millis())
                .certificationCode(uuidHolder.random())
                .build();
    }

    public User certificate(String certificationCode) {
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userName(userName)
                .userPhone(userPhone)
                .userAddr(userAddr)
                .userPwd(userPwd)
                .role(role)
                .status(UserStatus.ACTIVE)
                .userRegistrationDate(userRegistrationDate)
                .certificationCode(certificationCode)
                .build();

    }

    public void updateStatus(UserStatus newStatus) {
        if (this.status == UserStatus.PENDING) {
            this.status = newStatus;
        }
    }

}
