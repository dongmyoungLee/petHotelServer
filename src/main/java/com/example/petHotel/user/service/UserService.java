package com.example.petHotel.user.service;

import com.example.petHotel.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserCreate;
import com.example.petHotel.user.domain.UserStatus;
import com.example.petHotel.user.service.port.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;
    private final CertificationService certificationService;

    @Transactional
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate, uuidHolder, clockHolder);
        user = userRepository.save(user);

        certificationService.send(userCreate.getUserEmail(), user.getUserId(), user.getCertificationCode());

        return user;
    }
    @Transactional
    public void verifyEmail(UUID userId, String certificationCode) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("가입된 유저가 없습니다.", userId));

        if (!user.getCertificationCode().equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        userRepository.updateUserStatus(userId, UserStatus.ACTIVE);
    }

}
