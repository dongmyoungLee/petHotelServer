package com.example.petHotel.user.infrastructure;

import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUserIdAndStatus(UUID userId, UserStatus userStatus);
    Optional<UserEntity> findByUserEmail(String email);

}
