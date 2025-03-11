package com.example.petHotel.user.repository;

import com.example.petHotel.user.dto.UserDto;
import com.example.petHotel.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}