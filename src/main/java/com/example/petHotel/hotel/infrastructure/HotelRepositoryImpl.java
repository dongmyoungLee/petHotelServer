package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Hotel;
import com.example.petHotel.hotel.domain.HotelService;
import com.example.petHotel.hotel.domain.Room;
import com.example.petHotel.hotel.service.port.HotelRepository;
import com.example.petHotel.user.infrastructure.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public Optional<Hotel> findByCompanyId(UUID companyId) {
        return hotelJpaRepository.findByCompanyId(companyId).map(HotelEntity::toModel);
    }

    @Override
    public Hotel save(Hotel hotel) {
        return hotelJpaRepository.save(HotelEntity.fromModel(hotel)).toModel();
    }

}
