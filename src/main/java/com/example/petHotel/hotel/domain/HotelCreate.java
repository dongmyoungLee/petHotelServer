package com.example.petHotel.hotel.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class HotelCreate {
    private UUID companyId;
    private String hotelName;
    private String hotelAddress;
    private String hotelPhone;
    private String hotelWebsite;
    private String hotelOwnerName;
    private String hotelProfileImg;
    private Room rooms;
    private HotelService services;
}
