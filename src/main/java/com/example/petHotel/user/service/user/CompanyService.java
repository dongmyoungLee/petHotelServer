package com.example.petHotel.user.service.user;

import com.example.petHotel.common.domain.exception.DuplicateDataException;
import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.domain.exception.UsernameNotFoundException;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.domain.auth.UserToken;
import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.domain.company.CompanyCreate;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.service.port.auth.PasswordEncryption;
import com.example.petHotel.user.service.port.auth.RefreshTokenRepository;
import com.example.petHotel.user.service.port.company.CompanyRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final JwtProvider jwtProvider;

    public Company getById(UUID companyId) {
        return companyRepository.findByUserIdAndStatus(companyId, UserStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Companys", companyId));
    }

    @Transactional
    public Company create(CompanyCreate companyCreate) {

        if (companyRepository.findByUserEmail(companyCreate.getCompanyEmail()).isPresent()) {
            throw new DuplicateDataException();
        }

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

    public UserToken hotelLogin(String email, String password) {
        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(UsernameNotFoundException::new);


        if (!passwordEncryption.matches(password, company.getCompanyPwd())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        User tokenStructureTransfromation = User.builder()
                .userId(company.getCompanyId())
                .userEmail(company.getCompanyEmail())
                .userName(company.getCompanyName())
                .role(company.getRole())
                .build();

        String accessToken = jwtProvider.generateAccessToken(tokenStructureTransfromation);
        String refreshToken = jwtProvider.generateRefreshToken(tokenStructureTransfromation);

        // Refresh Token을 DB에 저장 (이전 토큰 무효화 가능)
//        refreshTokenRepository.save(RefreshToken.builder().userId(user.getUserId()).token(refreshToken).build());

        return UserToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(company.getRole())
                .id(company.getCompanyId())
                .name(company.getCompanyName())
                .email(company.getCompanyEmail())
                .build();
    }
}
