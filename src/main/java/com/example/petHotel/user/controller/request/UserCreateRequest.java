package com.example.petHotel.user.controller.request;

import com.example.petHotel.user.domain.user.Role;
import com.example.petHotel.user.domain.oauth.SnsType;
import com.example.petHotel.user.domain.user.UserCreate;
import com.example.petHotel.user.domain.user.UserStatus;
import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String userEmail;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddr;
    private UserStatus status;
    private Role role;

    public UserCreate to() {
        return UserCreate.builder()
                .userEmail(userEmail)
                .userPwd(userPwd)
                .userName(userName)
                .userPhone(userPhone)
                .userAddr(userAddr)
                .status(status)
                .role(role)
                .snsType(SnsType.DEFAULT)
                .build();
    }
}
