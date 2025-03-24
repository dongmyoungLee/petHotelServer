package com.example.petHotel.user.service.auth;

import com.example.petHotel.user.service.port.auth.MailSender;
import jakarta.mail.MessagingException;
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
    public void send(String email, UUID userId, String certificationCode) throws MessagingException {
        String certificationUrl = generateCertificationUrl(userId, certificationCode);
        String title = "PetHotel 가입 인증";
        String content = generateHtmlContent(certificationUrl);

        mailSender.send(email, title, content);
    }
    private String generateHtmlContent(String certificationUrl) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>🐾 PetHotel 가입 인증 🐾</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Arial', sans-serif;\n" +
                "            background-color: #f5f3ff; /* 연보라색 배경 */\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            min-height: 100vh;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 15px;\n" +
                "            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\n" +
                "            padding: 30px;\n" +
                "            text-align: center;\n" +
                "            max-width: 500px;\n" +
                "            border: 3px solid #8b74ff; /* 보라색 테두리 */\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            color: #8b74ff; /* 메인 보라색 */\n" +
                "            font-size: 22px;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            color: #555;\n" +
                "            font-size: 16px;\n" +
                "            margin-bottom: 20px;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            background-color: #8b74ff; /* 메인 보라색 버튼 */\n" +
                "            color: #ffffff;\n" +
                "            text-decoration: none;\n" +
                "            padding: 12px 25px;\n" +
                "            border-radius: 25px;\n" +
                "            font-size: 16px;\n" +
                "            font-weight: bold;\n" +
                "            box-shadow: 2px 4px 6px rgba(0, 0, 0, 0.1);\n" +
                "            transition: background-color 0.3s;\n" +
                "        }\n" +
                "\n" +
                "        .button:hover {\n" +
                "            background-color: #7a5de3; /* 더 진한 보라색 */\n" +
                "        }\n" +
                "\n" +
                "        .paw {\n" +
                "            font-size: 30px;\n" +
                "            color: #8b74ff;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"paw\">🐶🐾🐱</div>\n" +
                "        <h1>PetHotel 가입 인증</h1>\n" +
                "        <p>안녕하세요! PetHotel에 가입해 주셔서 감사합니다.</p>\n" +
                "        <p>아래 버튼을 클릭하여 인증을 완료해 주세요.</p>\n" +
                "        <a href=\"" + certificationUrl + "\" class=\"button\">🐾 로그인 인증하기 🐾</a>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }


    public String generateCertificationUrl(UUID userId, String certificationCode) {
        return "http://localhost:4000/api/v1/users/" + userId + "/verify?certificationCode=" + certificationCode;
    }
}
