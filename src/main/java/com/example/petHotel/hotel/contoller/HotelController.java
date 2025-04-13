package com.example.petHotel.hotel.contoller;

import com.example.petHotel.hotel.domain.HotelResponse;
import com.example.petHotel.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    @GetMapping("/{token}")
    public ResponseEntity<List<HotelResponse>> getHotelsByCompanyId(@PathVariable String token) {
        List<HotelResponse> hotels = hotelService.getHotelsByCompanyId(token);
        return ResponseEntity.status(HttpStatus.OK).body(hotels);
    }
}
