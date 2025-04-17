package com.example.petHotel.hotel.contoller;

import com.example.petHotel.hotel.contoller.request.request.HotelUpdateRequest;
import com.example.petHotel.hotel.contoller.response.CommonDefaultResponse;
import com.example.petHotel.hotel.domain.HotelResponse;
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
public class HotelController {
    private final HotelService hotelService;
    @GetMapping("/{token}")
    public ResponseEntity<List<HotelResponse>> getHotelsByCompanyId(@PathVariable String token) {
        List<HotelResponse> hotels = hotelService.getHotelsByCompanyId(token);
        return ResponseEntity.status(HttpStatus.OK).body(hotels);
    }

    @PutMapping
    public ResponseEntity<CommonDefaultResponse> putHotelByCompanyId(@RequestBody HotelUpdateRequest hotelUpdateRequest) {
        UUID updateHotelId = hotelService.putHotelByCompanyId(hotelUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(CommonDefaultResponse.builder()
                .statusCode(HttpStatus.OK)
                .msg(updateHotelId + ": 호텔이 수정 되었습니다.")
                .build());
    }
}
