package com.example.petHotel.user.service.domain;

import com.example.petHotel.user.dto.UserDto;
import com.example.petHotel.user.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDto {
    private UUID id;
    private String userEmail;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddr;
    private LocalDateTime userRegistrationDate;
    private Role role;

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userEmail(this.userEmail)
                .userPwd(this.userPwd)
                .userName(this.userName)
                .userPhone(this.userPhone)
                .userAddr(this.userAddr)
                .role(this.role)
                .userRegistrationDate(LocalDateTime.now())
                .build();
    }


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public String getUserPwd() {
        return userPwd;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserPhone() {
        return userPhone;
    }

    @Override
    public String getUserAddr() {
        return userAddr;
    }

    @Override
    public LocalDateTime getUserRegistrationDate() {
        return userRegistrationDate;
    }

    @Override
    public Role getRole() {
        return role;
    }
}