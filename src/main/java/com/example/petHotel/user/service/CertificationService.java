package com.example.petHotel.user.service;

import com.example.petHotel.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableAsync
public class CertificationService {
    private final MailSender mailSender;

    @Async
    public void send(String email, UUID userId, String certificationCode) {
        String CertificationUrl = generateCertificationUrl(userId, certificationCode);
        String title = "Please certify your email address";
        String content = "Please click the following link to certify your email address: " + CertificationUrl;

        mailSender.send(email, title, content);

    }

    public String generateCertificationUrl(UUID userId, String certificationCode) {
        return "http://localhost:4000/api/v1/users/" + userId + "/verify?certificationCode=" + certificationCode;
    }
}
