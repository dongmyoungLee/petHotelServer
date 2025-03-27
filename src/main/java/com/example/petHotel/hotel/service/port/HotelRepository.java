package com.example.petHotel.hotel.service.port;

import com.example.petHotel.hotel.domain.Hotel;

import java.util.Optional;
import java.util.UUID;

public interface HotelRepository {
    Optional<Hotel> findById(UUID hotelId);
    Optional<Hotel> findByCompanyId(UUID companyId);
    Hotel save(Hotel hotel);
}
