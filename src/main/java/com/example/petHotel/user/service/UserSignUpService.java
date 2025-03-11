package com.example.petHotel.user.service;

import com.example.petHotel.user.controller.model.UserDtoImpl;
import com.example.petHotel.user.dto.UserDto;
import com.example.petHotel.user.entities.UserEntity;
import com.example.petHotel.user.repository.UserRepository;
import com.example.petHotel.user.service.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignUpService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserDto createUser(UserDto userDto) {
        // 비밀번호 암호화
        String encodedPassword = passwordService.encodePassword(userDto.getUserPwd());

        // DTO → User(도메인 모델) 변환
        User user = User.builder()
                .userEmail(userDto.getUserEmail())
                .userPwd(encodedPassword)
                .userName(userDto.getUserName())
                .userPhone(userDto.getUserPhone())
                .userAddr(userDto.getUserAddr())
                .role(userDto.getRole())
                .build();

        // 저장
        UserEntity savedUser = userRepository.save(user.toEntity());

        // Entity → DTO 변환 후 반환
        return UserDtoImpl.builder()
                .id(savedUser.getId())
                .userEmail(savedUser.getUserEmail())
                .userName(savedUser.getUserName())
                .userPhone(savedUser.getUserPhone())
                .userAddr(savedUser.getUserAddr())
                .role(savedUser.getRole())
                .userRegistrationDate(savedUser.getUserRegistrationDate())
                .build();
    }
}