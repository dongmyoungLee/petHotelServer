package com.example.petHotel.hotel.service.port;

import com.example.petHotel.hotel.domain.Room;

public interface RoomRepository {
    Room save(Room room);
}
