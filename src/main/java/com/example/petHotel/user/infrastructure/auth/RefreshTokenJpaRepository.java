package com.example.petHotel.user.infrastructure.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long>  {

    Optional<RefreshTokenEntity> findByUserId(UUID userId);

}
