package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.HotelServiceDomain;
import com.example.petHotel.hotel.domain.Room;
import com.example.petHotel.hotel.service.HotelService;
import com.example.petHotel.hotel.service.port.HotelServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HotelServiceRepositoryImpl implements HotelServiceRepository {
    private final HotelServiceJpaRepository hotelServiceJpaRepository;
    @Override
    public HotelServiceDomain save(HotelServiceDomain hotelServiceDomain) {
        return hotelServiceJpaRepository.save(HotelServiceEntity.fromModel(hotelServiceDomain)).toModel();
    }

    @Override
    public List<HotelServiceDomain> saveAll(List<HotelServiceDomain> hotelService) {
        List<HotelServiceEntity> roomEntities = hotelService.stream()
                .map(HotelServiceEntity::fromModel)
                .collect(Collectors.toList());
        hotelServiceJpaRepository.saveAll(roomEntities);
        return hotelService;
    }

    @Override
    public List<HotelServiceDomain> findByHotelIdIn(List<UUID> hotelIds) {
        return hotelServiceJpaRepository.findByHotel_HotelIdIn(hotelIds).stream()
                .map(HotelServiceEntity::toModel)
                .collect(Collectors.toList());
    }
}
