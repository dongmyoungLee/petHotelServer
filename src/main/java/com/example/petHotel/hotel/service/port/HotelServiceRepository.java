package com.example.petHotel.hotel.service.port;

import com.example.petHotel.hotel.domain.HotelService;

public interface HotelServiceRepository {
    HotelService save(HotelService hotelService);
}
