package com.example.petHotel.user.dto;

import com.example.petHotel.user.service.domain.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserDto {
    UUID getId();
    String getUserEmail();
    String getUserPwd();
    String getUserName();
    String getUserPhone();
    String getUserAddr();
    LocalDateTime getUserRegistrationDate();
    Role getRole();
}