package com.example.petHotel.user.controller.request;

import lombok.Getter;

@Getter
public class UserLoginRequest {
    private String userEmail;
    private String userPwd;
}
