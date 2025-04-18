package com.example.petHotel.user.infrastructure.auth;

import com.example.petHotel.user.domain.auth.RefreshToken;
import com.example.petHotel.user.service.port.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByUserId(UUID userId) {
        return refreshTokenJpaRepository.findByUserId(userId).map(RefreshTokenEntity::toModel);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(RefreshTokenEntity.fromModel(refreshToken)).toModel();
    }
}
