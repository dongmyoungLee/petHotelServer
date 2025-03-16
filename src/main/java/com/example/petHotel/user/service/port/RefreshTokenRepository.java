package com.example.petHotel.user.service.port;

import com.example.petHotel.user.domain.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUserId(UUID userId);

    RefreshToken save(RefreshToken refreshToken);
}
