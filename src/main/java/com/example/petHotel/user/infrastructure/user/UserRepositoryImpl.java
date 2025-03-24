package com.example.petHotel.user.infrastructure.user;

import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;
import com.example.petHotel.user.service.port.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.fromModel(user)).toModel();
    }

    @Override
    public Optional<User> findByUserIdAndStatus(UUID userId, UserStatus userStatus) {
        return userJpaRepository.findByUserIdAndStatus(userId, userStatus).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByUserEmail(String email) {
        return userJpaRepository.findByUserEmail(email).map(UserEntity::toModel);
    }


}
