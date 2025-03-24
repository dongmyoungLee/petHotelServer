package com.example.petHotel.user.service.port.auth;

import com.example.petHotel.user.domain.auth.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUserId(UUID userId);

    RefreshToken save(RefreshToken refreshToken);
}
