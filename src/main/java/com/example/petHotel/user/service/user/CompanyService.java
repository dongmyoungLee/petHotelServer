package com.example.petHotel.user.service.user;

import com.example.petHotel.common.domain.exception.DuplicateDataException;
import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.domain.company.CompanyCreate;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.service.port.auth.PasswordEncryption;
import com.example.petHotel.user.service.port.auth.RefreshTokenRepository;
import com.example.petHotel.user.service.port.company.CompanyRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Builder
@Getter
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;
    private final PasswordEncryption passwordEncryption;

    public Company getById(UUID companyId) {
        return companyRepository.findByUserIdAndStatus(companyId, UserStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Companys", companyId));
    }

    @Transactional
    public Company create(CompanyCreate companyCreate) throws MessagingException {
        Company company = Company.from(companyCreate, uuidHolder, clockHolder, passwordEncryption);
        company = companyRepository.save(company);

        return company;
    }

    @Transactional
    public void verifyEmail(UUID companyId) {

        companyRepository.findByUserIdAndStatus(companyId, UserStatus.ACTIVE).ifPresent(company -> {
            throw new DuplicateDataException();
        });

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("가입된 기업이 없습니다.", companyId));

        companyRepository.save(company);
    }
}
