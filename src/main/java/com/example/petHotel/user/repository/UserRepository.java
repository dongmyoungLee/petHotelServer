package com.example.petHotel.user.repository;

import com.example.petHotel.user.dto.UserDto;

public interface UserRepository {
    UserDto save(UserDto userDto);
}