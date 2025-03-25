package com.example.petHotel.user.controller.request;

import com.example.petHotel.user.domain.company.CompanyCreate;
import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.user.UserStatus;
import lombok.Getter;

@Getter
public class CompanyCreateRequest {
    private String companyEmail;
    private String companyPwd;
    private String companyName;
    private String companyPhone;
    private UserStatus status;
    private Role role;
    public CompanyCreate to() {
        return CompanyCreate.builder()
                .companyEmail(companyEmail)
                .companyPwd(companyPwd)
                .companyName(companyName)
                .companyPhone(companyPhone)
                .status(status)
                .role(role)
                .build();
    }

}
