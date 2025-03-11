package com.example.petHotel.user.controller;

import com.example.petHotel.user.controller.model.UserDtoImpl;
import com.example.petHotel.user.dto.UserDto;
import com.example.petHotel.user.service.UserSignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserSignUpController {
    private final UserSignUpService userSignUpService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody UserDtoImpl userDto) {
        UserDto createdUserDto = userSignUpService.createUser(userDto);
        return ResponseEntity.ok(createdUserDto);
    }
}