package com.example.petHotel.hotel.service.port;

import com.example.petHotel.hotel.domain.HotelServiceDomain;

import java.util.List;
import java.util.UUID;

public interface HotelServiceRepository {
    HotelServiceDomain save(HotelServiceDomain hotelServiceDomain);

    List<HotelServiceDomain> saveAll(List<HotelServiceDomain> hotelService);

    List<HotelServiceDomain> findByHotelIdIn(List<UUID> hotelIds);
}
