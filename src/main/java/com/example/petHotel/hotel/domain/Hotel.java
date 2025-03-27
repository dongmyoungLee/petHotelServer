package com.example.petHotel.hotel.domain;

import com.example.petHotel.common.service.ClockHolder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Hotel {
    private UUID hotelId;
    private UUID companyId;
    private String hotelName;
    private String hotelAddress;
    private String hotelPhone;
    private String hotelWebsite;
    private String hotelOwnerName;
    private String hotelProfileImg;
    private long hotelRegistrationDate;
    private List<Room> rooms;
    private List<HotelServiceDomain> services;

    public static Hotel from(HotelCreate hotelCreate, ClockHolder clockHolder) {
        return Hotel.builder()
                .companyId(hotelCreate.getCompanyId())
                .hotelName(hotelCreate.getHotelName())
                .hotelAddress(hotelCreate.getHotelAddress())
                .hotelPhone(hotelCreate.getHotelPhone())
                .hotelWebsite(hotelCreate.getHotelWebsite())
                .hotelOwnerName(hotelCreate.getHotelOwnerName())
                .hotelProfileImg(hotelCreate.getHotelProfileImg())
                .hotelRegistrationDate(clockHolder.millis())
                .rooms(hotelCreate.getRooms())
                .services(hotelCreate.getServices())
                .build();
    }
}
