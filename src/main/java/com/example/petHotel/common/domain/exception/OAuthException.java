package com.example.petHotel.common.domain.exception;

public class OAuthException extends RuntimeException {
    public OAuthException(String message) {
        super(message);
    }
}