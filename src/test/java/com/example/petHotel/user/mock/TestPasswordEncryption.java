package com.example.petHotel.user.mock;

import com.example.petHotel.user.service.port.PasswordEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class TestPasswordEncryption implements PasswordEncryption {
    private final PasswordEncoder passwordEncoder;
    @Override
    public String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
