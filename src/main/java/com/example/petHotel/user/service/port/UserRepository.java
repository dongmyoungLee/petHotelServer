package com.example.petHotel.user.service.port;

import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserStatus;
import com.example.petHotel.user.infrastructure.UserEntity;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID userId);
    User save(User user);
    Optional<User> findByUserIdAndStatus(UUID userId, UserStatus userStatus);
    Optional<User> findByUserEmail(String email);
}
