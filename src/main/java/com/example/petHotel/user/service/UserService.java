package com.example.petHotel.user.service;

import com.example.petHotel.user.dto.UserDto;
import com.example.petHotel.user.repository.UserRepository;
import com.example.petHotel.user.service.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        User user = new User(userDto.getId(), userDto.getName(), userDto.getEmail());
        return userRepository.save(user);
    }
}