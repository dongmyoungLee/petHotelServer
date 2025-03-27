package com.example.petHotel.hotel.service;

import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.hotel.domain.Room;
import com.example.petHotel.hotel.service.port.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ClockHolder clockHolder;

    @Transactional
    public void saveRooms(List<Room> rooms, UUID hotelId) {
        List<Room> roomEntities = rooms.stream()
                .map(room -> Room.builder()
                        .roomType(room.getRoomType())
                        .roomPrice(room.getRoomPrice())
                        .roomStatus(room.getRoomStatus())
                        .roomDescription(room.getRoomDescription())
                        .dayCare(room.getDayCare())
                        .hotelId(hotelId)
                        .roomRegistrationDate(clockHolder.millis())
                        .build())
                .toList();
        roomRepository.saveAll(roomEntities);
    }
}
