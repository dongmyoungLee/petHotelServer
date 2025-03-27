package com.example.petHotel.hotel.service.port;

import com.example.petHotel.hotel.domain.Room;

import java.util.List;
import java.util.UUID;

public interface RoomRepository {
    Room save(Room room);

    List<Room> saveAll(List<Room> rooms);

    List<Room> findByHotelIdIn(List<UUID> hotelIds);
}
