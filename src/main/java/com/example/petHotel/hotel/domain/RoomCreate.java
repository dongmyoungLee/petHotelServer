package com.example.petHotel.hotel.domain;

import com.example.petHotel.hotel.infrastructure.HotelEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class RoomCreate {
    private String roomType;
    private Integer roomPrice;
    private RoomStatus roomStatus;
    private String roomDescription;
    private String dayCare;
    private UUID hotelId;
}
