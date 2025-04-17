package com.example.petHotel.hotel.domain;

import com.example.petHotel.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.hotel.contoller.request.request.HotelUpdateRequest;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;
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

    public Hotel updateHotel(HotelUpdateRequest hotelUpdateRequest) {
        return Hotel.builder()
                // 기존 값 유지
                .hotelId(this.hotelId)
                .companyId(this.companyId)
                .hotelRegistrationDate(this.hotelRegistrationDate)
                .rooms(this.rooms)
                .services(this.services)
                // 변경 가능한 필드
                .hotelName(hotelUpdateRequest.getHotelName())
                .hotelAddress(hotelUpdateRequest.getHotelAddress())
                .hotelPhone(hotelUpdateRequest.getHotelPhone())
                .hotelWebsite(hotelUpdateRequest.getHotelWebsite())
                .hotelOwnerName(hotelUpdateRequest.getHotelOwnerName())
                .hotelProfileImg(hotelUpdateRequest.getHotelProfileImg())
                .build();
    }

}
