package com.example.petHotel.user.controller.response;

import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserCreateResponse {
    private UUID userId;
    private String userEmail;
    private String userName;
    private String userPhone;
    private String userAddr;
    private UserStatus userStatus;
    private long userRegistrationDate;

    public static UserCreateResponse from(User user) {
        return UserCreateResponse.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .userStatus(user.getStatus())
                .userPhone(user.getUserPhone())
                .userRegistrationDate(user.getUserRegistrationDate())
                .build();
    }
}
