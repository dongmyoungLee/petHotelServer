package com.example.petHotel.hotel.domain;

import com.example.petHotel.common.service.ClockHolder;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Room {
    private UUID roomId;
    private String roomType;
    private Integer roomPrice;
    private RoomStatus roomStatus;
    private String roomDescription;
    private String dayCare;
    private long roomRegistrationDate;
    private UUID hotelId;

    public static Room from(RoomCreate roomCreate, ClockHolder clockHolder) {
        return Room.builder()
                .roomType(roomCreate.getRoomType())
                .roomPrice(roomCreate.getRoomPrice())
                .roomStatus(roomCreate.getRoomStatus())
                .roomDescription(roomCreate.getRoomDescription())
                .dayCare(roomCreate.getDayCare())
                .roomRegistrationDate(clockHolder.millis())
                .hotelId(roomCreate.getHotelId())
                .build();
    }
}
