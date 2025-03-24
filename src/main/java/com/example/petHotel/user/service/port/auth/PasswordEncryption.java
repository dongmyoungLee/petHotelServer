package com.example.petHotel.user.service.port.auth;

public interface PasswordEncryption {
    String encryptPassword(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
