package com.example.petHotel.user.infrastructure.user;

import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.infrastructure.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUserIdAndStatus(UUID userId, UserStatus userStatus);
    Optional<UserEntity> findByUserEmail(String email);

}
