package com.example.petHotel.user.service;

import com.example.petHotel.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificationService {
    private final MailSender mailSender;

    public void send(String email, UUID userId, String certificationCode) {
        String CertificationUrl = generateCertificationUrl(userId, certificationCode);
        String title = "Please certify your email address";
        String content = "Please click the following link to certify your email address: " + CertificationUrl;

        mailSender.send(email, title, content);

    }

    public String generateCertificationUrl(UUID userId, String certificationCode) {
        return "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationCode;
    }
}
