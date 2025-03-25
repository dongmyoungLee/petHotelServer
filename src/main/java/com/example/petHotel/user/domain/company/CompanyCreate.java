package com.example.petHotel.user.domain.company;

import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.user.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompanyCreate {
    private String companyEmail;
    private String companyPwd;
    private String companyName;
    private String companyPhone;
    private UserStatus status;
    private Role role;
}
