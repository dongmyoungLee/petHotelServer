package com.example.petHotel.user.infrastructure.company;

import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.infrastructure.user.UserEntity;
import com.example.petHotel.user.service.port.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {
    private final CompanyJpaRepository companyJpaRepository;
    @Override
    public Optional<Company> findById(UUID companyId) {
        return companyJpaRepository.findById(companyId).map(CompanyEntity::toModel);
    }

    @Override
    public Company save(Company company) {
        return companyJpaRepository.save(CompanyEntity.fromModel(company)).toModel();
    }

    @Override
    public Optional<Company> findByUserIdAndStatus(UUID companyId, UserStatus CompanyStatus) {
        return companyJpaRepository.findByCompanyIdAndStatus(companyId, CompanyStatus).map(CompanyEntity::toModel);
    }

    @Override
    public Optional<Company> findByUserEmail(String CompanyEmail) {
        return companyJpaRepository.findByCompanyEmail(CompanyEmail).map(CompanyEntity::toModel);
    }
}
