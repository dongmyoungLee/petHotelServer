package com.example.petHotel.user.domain.auth;

import com.example.petHotel.user.domain.user.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserToken {
    private String accessToken;
    private String refreshToken;
    private Role role;
    private UUID id;
    private String name;
    private String email;
}