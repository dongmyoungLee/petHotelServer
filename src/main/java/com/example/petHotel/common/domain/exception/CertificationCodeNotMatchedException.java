package com.example.petHotel.common.domain.exception;

public class CertificationCodeNotMatchedException extends RuntimeException {

    public CertificationCodeNotMatchedException() {
        super("자격 증명 실패");
    }
}
