package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.HotelServiceDomain;
import com.example.petHotel.hotel.service.HotelService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HotelServiceJpaRepository extends JpaRepository<HotelServiceEntity, UUID> {

    List<HotelServiceEntity> findByHotel_HotelIdIn(List<UUID> hotelIds);
}
