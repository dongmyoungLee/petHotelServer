package com.example.petHotel.hotel.service.port;

import com.example.petHotel.hotel.domain.Hotel;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HotelRepository {
    Optional<Hotel> findById(UUID hotelId);
    List<Hotel> findAllByByCompanyId(UUID companyId);
    Hotel save(Hotel hotel);
}
