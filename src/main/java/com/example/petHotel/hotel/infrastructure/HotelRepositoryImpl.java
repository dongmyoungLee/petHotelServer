package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Hotel;
import com.example.petHotel.hotel.service.port.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HotelRepositoryImpl implements HotelRepository {
    private final HotelJpaRepository hotelJpaRepository;
    private final RoomJpaRepository roomJpaRepository;
    private final HotelServiceJpaRepository hotelServiceJpaRepository;

    @Override
    public Optional<Hotel> findById(UUID hotelId) {
        return hotelJpaRepository.findById(hotelId).map(HotelEntity::toModel);
    }

    @Override
    public List<Hotel> findAllByCompanyId(UUID companyId) {
        return hotelJpaRepository.findAllByCompanyId(companyId).stream().map(HotelEntity::toModel).toList();
    }


//    @Override
//    public List<Hotel> findAllByByCompanyId(UUID companyId) {
//        return hotelJpaRepository.findAllByByCompanyId(companyId).stream().map(HotelEntity::toModel);
//    }

    @Override
    public Hotel save(Hotel hotel) {
        return hotelJpaRepository.save(HotelEntity.fromModel(hotel)).toModel();
    }

}
