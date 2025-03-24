package com.example.petHotel.user.controller.user;

import com.example.petHotel.user.controller.request.UserCreateRequest;
import com.example.petHotel.user.controller.response.UserCreateResponse;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.service.user.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserCreateController {

    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest userCreateRequest) throws MessagingException {
        // UserCreateRequest 는 책임과 역할을 분리하기 위해 UserCreate와 분리..
        // User 는 의존성이 domain으로 흐르기 때문에 사용..
        User user = userService.create(userCreateRequest.to());

        return ResponseEntity.status(HttpStatus.CREATED).body(UserCreateResponse.from(user));
    }
}
