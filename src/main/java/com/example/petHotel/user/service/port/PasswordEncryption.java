package com.example.petHotel.user.service.port;

public interface PasswordEncryption {
    String encryptPassword(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
