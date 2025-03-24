package com.example.petHotel.user.infrastructure.company;

import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.domain.oauth.SnsType;
import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.infrastructure.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "companys")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID companyId;

    @Column(name = "company_email", nullable = false, unique = true)
    private String companyEmail;

    @Column(name = "company_pass")
    private String companyPwd;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_phone")
    private String companyPhone;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "company_registration_date", nullable = false)
    private long companyRegistrationDate;

    public Company toModel() {
        return Company.builder()
                .companyId(companyId)
                .companyEmail(companyEmail)
                .companyPwd(companyPwd)
                .companyName(companyName)
                .companyPhone(companyPhone)
                .status(status)
                .role(role)
                .companyRegistrationDate(companyRegistrationDate)
                .build();
    }

    public static CompanyEntity fromModel(Company company) {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.companyId = company.getCompanyId();
        companyEntity.companyEmail = company.getCompanyEmail();
        companyEntity.companyPwd = company.getCompanyPwd();
        companyEntity.companyName = company.getCompanyName();
        companyEntity.companyPhone = company.getCompanyPhone();
        companyEntity.status = company.getStatus();
        companyEntity.role = company.getRole();
        companyEntity.companyRegistrationDate = company.getCompanyRegistrationDate();
        return companyEntity;
    }
}
