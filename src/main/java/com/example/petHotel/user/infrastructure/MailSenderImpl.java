package com.example.petHotel.user.infrastructure;

import com.example.petHotel.user.service.port.MailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final JavaMailSender javaMailSender;
    @Override
    public void send(String email, String title, String content) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setTo(email);
        helper.setSubject(title);
        helper.setText(content, true);
//        message.setTo(email);
//        message.setSubject(title);
//        message.setText(content);
        javaMailSender.send(message);
    }
}
