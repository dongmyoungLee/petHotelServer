package com.example.petHotel.hotel.contoller.request.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HotelUpdateRequest {
    private UUID hotelId;
    private UUID companyId;
    private String hotelName;
    private String hotelAddress;
    private String hotelPhone;
    private String hotelWebsite;
    private String hotelOwnerName;
    private String hotelProfileImg;
}
