package com.example.petHotel.hotel.contoller;

import com.example.petHotel.hotel.contoller.request.HotelCreateRequest;
import com.example.petHotel.hotel.contoller.response.HotelCreateResponse;
import com.example.petHotel.hotel.domain.HotelResponse;
import com.example.petHotel.hotel.domain.Hotel;
import com.example.petHotel.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelCreateController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelCreateResponse> createUser(@RequestBody HotelCreateRequest hotelCreateRequest) {
        Hotel hotel = hotelService.createHotel(hotelCreateRequest.to());
        return ResponseEntity.status(HttpStatus.CREATED).body(HotelCreateResponse.from(hotel));
    }


}
