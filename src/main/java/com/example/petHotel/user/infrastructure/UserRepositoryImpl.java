package com.example.petHotel.user.infrastructure;

import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserStatus;
import com.example.petHotel.user.service.port.UserRepository;
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
    public void updateUserStatus(UUID userId, UserStatus status) {
        userJpaRepository.updateUserStatus(userId, status);
    }


}
