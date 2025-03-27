package com.example.petHotel.user.domain.company;

import com.example.petHotel.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserCreate;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.service.port.auth.PasswordEncryption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class Company {
    private UUID companyId;
    private String companyEmail;
    private String companyPwd;
    private String companyName;
    private String companyPhone;
    private UserStatus status;
    private Role role;
    private long companyRegistrationDate;

    public static Company from(CompanyCreate companyCreate, UuidHolder uuidHolder, ClockHolder clockHolder, PasswordEncryption passwordEncryption) {
        return Company.builder()
                .companyEmail(companyCreate.getCompanyEmail())
                .companyPwd(passwordEncryption.encryptPassword(companyCreate.getCompanyPwd()))
                .companyName(companyCreate.getCompanyName())
                .companyPhone(companyCreate.getCompanyPhone())
                .role(companyCreate.getRole())
                .status(companyCreate.getStatus())
                .companyRegistrationDate(clockHolder.millis())
                .build();
    }
}
