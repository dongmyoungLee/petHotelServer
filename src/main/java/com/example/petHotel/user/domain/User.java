package com.example.petHotel.user.domain;

import com.example.petHotel.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.service.port.PasswordEncryption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private SnsType snsType;

    public static User from(UserCreate userCreate, UuidHolder uuidHolder, ClockHolder clockHolder, PasswordEncryption passwordEncryption) {
        return User.builder()
                .userEmail(userCreate.getUserEmail())
                .userPwd(userCreate.getUserPwd().equals("sns") ? null : passwordEncryption.encryptPassword(userCreate.getUserPwd()))
                .userName(userCreate.getUserName())
                .userPhone(userCreate.getUserPhone())
                .userAddr(userCreate.getUserAddr())
                .role(userCreate.getRole())
                .snsType(userCreate.getSnsType())
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
                .userPwd(userPwd)
                .userName(userName)
                .userPhone(userPhone)
                .userAddr(userAddr)
                .role(role)
                .status(UserStatus.ACTIVE)
                .userRegistrationDate(userRegistrationDate)
                .certificationCode(certificationCode)
                .build();

    }


}
