package com.example.petHotel.user.service.port.company;

import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.domain.user.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Optional<Company> findById(UUID companyId);
    Company save(Company company);
    Optional<Company> findByUserIdAndStatus(UUID companyId, UserStatus CompanyStatus);
    Optional<Company> findByUserEmail(String CompanyEmail);
}
