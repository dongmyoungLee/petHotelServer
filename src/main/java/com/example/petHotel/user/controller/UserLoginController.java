package com.example.petHotel.user.controller;

import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.user.domain.Role;
import com.example.petHotel.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserLoginController {
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public void login() {
        User testUser = User.builder()
                .userId(UUID.randomUUID())
                .userEmail("pajang2222@naver.com")
                .role(Role.CUSTOMER)
                .userName("DM")
                .build();
        String access = jwtProvider.generateToken(testUser, "access");

        System.out.println(access);

        //return ResponseEntity.status(HttpStatus.CREATED).body(UserCreateResponse.from(user));
    }
}
