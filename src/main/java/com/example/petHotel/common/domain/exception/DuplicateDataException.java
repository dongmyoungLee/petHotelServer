package com.example.petHotel.common.domain.exception;

public class DuplicateDataException extends RuntimeException{
    public DuplicateDataException() {
        super("중복 데이터 입니다.");
    }
}
