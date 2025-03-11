package com.example.petHotel.user.repository;

import com.example.petHotel.user.controller.model.UserDtoImpl;
import com.example.petHotel.user.dto.UserDto;
import com.example.petHotel.user.entities.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDto save(UserDto userDto) {
        UserEntity userEntity = new UserEntity(userDto.getId(), userDto.getName(), userDto.getEmail());
        if (userEntity.getId() == null) {
            entityManager.persist(userEntity);
        } else {
            userEntity = entityManager.merge(userEntity);
        }
        return new UserDtoImpl(userEntity.getId(), userEntity.getName(), userEntity.getEmail());
    }
}