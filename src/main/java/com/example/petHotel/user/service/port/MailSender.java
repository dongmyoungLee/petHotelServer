package com.example.petHotel.user.service.port;

import jakarta.mail.MessagingException;

public interface MailSender {
    void send(String email, String title, String content) throws MessagingException;
}
