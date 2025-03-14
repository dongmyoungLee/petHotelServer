package com.example.petHotel.user.mock;

import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserStatus;
import com.example.petHotel.user.service.port.UserRepository;

import java.util.*;

public class FakeUserRepository implements UserRepository {
    private final List<User> data = new ArrayList<>();

    @Override
    public Optional<User> findById(UUID userId) {
        return data.stream().filter(item -> item.getUserId().equals(userId)).findAny();
    }

    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            User newUser = User.builder()
                    .userId(UUID.randomUUID())
                    .userEmail(user.getUserEmail())
                    .userPwd(user.getUserPwd())
                    .userPhone(user.getUserPhone())
                    .role(user.getRole())
                    .userName(user.getUserName())
                    .userAddr(user.getUserAddr())
                    .certificationCode(user.getCertificationCode())
                    .status(user.getStatus())
                    .userRegistrationDate(user.getUserRegistrationDate())
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getUserId(), user.getUserId()));
            data.add(user);
            return user;
        }
    }


    @Override
    public Optional<User> findByUserIdAndStatus(UUID userId, UserStatus userStatus) {
        return data.stream()
                .filter(user -> user.getUserId().equals(userId) && user.getStatus().equals(userStatus))
                .findFirst();
    }
}
