package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Room;
import com.example.petHotel.hotel.service.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {
    private final RoomJpaRepository roomJpaRepository;
    @Override
    public Room save(Room room) {
        return roomJpaRepository.save(RoomEntity.fromModel(room)).toModel();
    }
}
