package com.example.petHotel.user.service.port;

import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserStatus;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);
    User save(User user);

    void updateUserStatus(UUID userId,UserStatus status);
}
