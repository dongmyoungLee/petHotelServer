package com.example.petHotel.user.service.user;

import com.example.petHotel.common.domain.exception.DuplicateDataException;
import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.common.service.UuidHolder;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserCreate;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.service.auth.CertificationService;
import com.example.petHotel.user.service.port.auth.PasswordEncryption;
import com.example.petHotel.user.service.port.auth.RefreshTokenRepository;
import com.example.petHotel.user.service.port.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Builder
@Getter
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;
    private final PasswordEncryption passwordEncryption;
    private final CertificationService certificationService;

    public User getById(UUID userId) {
        return userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Users", userId));
    }

    @Transactional
    public User create(UserCreate userCreate) throws MessagingException {

        if (userRepository.findByUserEmail(userCreate.getUserEmail()).isPresent()) {
            throw new DuplicateDataException();
        }

        User user = User.from(userCreate, uuidHolder, clockHolder, passwordEncryption);
        user = userRepository.save(user);
        certificationService.send(userCreate.getUserEmail(), user.getUserId(), user.getCertificationCode());

        return user;
    }
    @Transactional
    public void verifyEmail(UUID userId, String certificationCode) {

        userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE).ifPresent(user -> {
                    throw new DuplicateDataException();
            });

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("가입된 유저가 없습니다.", userId));

        user = user.certificate(certificationCode);

        userRepository.save(user);
    }

}
