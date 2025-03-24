package com.example.petHotel.user.service.port.user;

import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID userId);
    User save(User user);
    Optional<User> findByUserIdAndStatus(UUID userId, UserStatus userStatus);
    Optional<User> findByUserEmail(String email);
}
