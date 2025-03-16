package com.example.petHotel.user.controller.request;

import com.example.petHotel.user.domain.Role;
import lombok.Getter;

@Getter
public class UserLoginRequest {
    private String userEmail;
    private String userPwd;
}
