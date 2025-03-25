package com.example.petHotel.common.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException() {
        super("등록되지 않은 사용자 입니다.");
    }
}
