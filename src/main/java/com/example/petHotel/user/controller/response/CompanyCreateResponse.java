package com.example.petHotel.user.controller.response;

import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyCreateResponse {
    private UUID companyId;
    private String companyEmail;
    private String companyName;
    private String companyPhone;
    private UserStatus companyStatus;
    private long companyRegistrationDate;

    public static CompanyCreateResponse from(Company company) {
        return CompanyCreateResponse.builder()
                .companyId(company.getCompanyId())
                .companyEmail(company.getCompanyEmail())
                .companyName(company.getCompanyName())
                .companyStatus(company.getStatus())
                .companyPhone(company.getCompanyPhone())
                .companyRegistrationDate(company.getCompanyRegistrationDate())
                .build();
    }
}
