package com.example.petHotel.user.infrastructure;

import com.example.petHotel.user.domain.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.status = :status WHERE u.userId = :userId")
    void updateUserStatus(@Param("userId") UUID userId, @Param("status") UserStatus status);

    Optional<UserEntity> findByUserIdAndStatus(UUID userId, UserStatus userStatus);
}
