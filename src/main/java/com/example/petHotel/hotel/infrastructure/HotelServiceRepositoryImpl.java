package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.HotelService;
import com.example.petHotel.hotel.service.port.HotelServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HotelServiceRepositoryImpl implements HotelServiceRepository {
    private final HotelServiceJpaRepository hotelServiceJpaRepository;
    @Override
    public HotelService save(HotelService hotelService) {
        return hotelServiceJpaRepository.save(HotelServiceEntity.fromModel(hotelService)).toModel();
    }
}
