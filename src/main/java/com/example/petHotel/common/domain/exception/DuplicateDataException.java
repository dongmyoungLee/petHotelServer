package com.example.petHotel.common.domain.exception;

public class DuplicateDataException extends RuntimeException{
    public DuplicateDataException() {
        super("이미 존재하는 데이터 입니다.");
    }
}
