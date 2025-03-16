package com.example.petHotel.common.domain.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String datasource, UUID id) {
        super(datasource + "에서 ID " + id + "를 찾을 수 없습니다.");
    }
}
