package com.example.petHotel.user.service.port;

import com.example.petHotel.user.domain.User;

public interface UserRepository {
    User save(User user);
}
