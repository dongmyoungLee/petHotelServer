package com.example.petHotel.hotel.contoller.response;

import com.example.petHotel.hotel.domain.Hotel;
import com.example.petHotel.hotel.domain.HotelServiceDomain;
import com.example.petHotel.hotel.domain.Room;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class HotelCreateResponse {
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

    public static HotelCreateResponse from(Hotel hotel) {
        return HotelCreateResponse.builder()
                .hotelId(hotel.getHotelId())
                .companyId(hotel.getCompanyId())
                .hotelName(hotel.getHotelName())
                .hotelAddress(hotel.getHotelAddress())
                .hotelPhone(hotel.getHotelPhone())
                .hotelWebsite(hotel.getHotelWebsite())
                .hotelOwnerName(hotel.getHotelOwnerName())
                .hotelProfileImg(hotel.getHotelProfileImg())
                .hotelRegistrationDate(hotel.getHotelRegistrationDate())
                .rooms(hotel.getRooms())
                .services(hotel.getServices())
                .build();
    }

}
