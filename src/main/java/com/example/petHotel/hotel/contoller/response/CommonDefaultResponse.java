package com.example.petHotel.hotel.contoller.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class CommonDefaultResponse {
    private HttpStatus statusCode;
    private String msg;
}
