package com.example.petHotel.user.infrastructure.company;

import com.example.petHotel.user.domain.user.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByCompanyIdAndStatus(UUID userId, UserStatus userStatus);
    Optional<CompanyEntity> findByCompanyEmail(String email);

}
