package com.example.petHotel.user.domain.user;

import com.example.petHotel.user.domain.oauth.SnsType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCreate {
    private String userEmail;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddr;
    private UserStatus status;
    private Role role;
    private SnsType snsType;
}
