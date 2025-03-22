package com.example.petHotel.common.domain.exception;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException() {
        super("없는 사용자 입니다.");
    }
}
