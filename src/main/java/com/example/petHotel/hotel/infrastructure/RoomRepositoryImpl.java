package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Room;
import com.example.petHotel.hotel.service.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {
    private final RoomJpaRepository roomJpaRepository;
    @Override
    public Room save(Room room) {
        return roomJpaRepository.save(RoomEntity.fromModel(room)).toModel();
    }

    @Override
    public List<Room> saveAll(List<Room> rooms) {
        List<RoomEntity> roomEntities = rooms.stream()
                .map(RoomEntity::fromModel)
                .collect(Collectors.toList());
        roomJpaRepository.saveAll(roomEntities);
        return rooms;
    }

    @Override
    public List<Room> findByHotelIdIn(List<UUID> hotelIds) {
        return roomJpaRepository.findByHotel_HotelIdIn(hotelIds).stream().map(RoomEntity::toModel).collect(Collectors.toList());
    }
}
