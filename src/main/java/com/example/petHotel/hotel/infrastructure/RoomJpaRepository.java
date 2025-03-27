package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

    List<RoomEntity> findByHotel_HotelIdIn(List<UUID> hotelIds);
}
