package com.example.petHotel.hotel.contoller.request;

import com.example.petHotel.hotel.domain.HotelCreate;
import com.example.petHotel.hotel.domain.HotelService;
import com.example.petHotel.hotel.domain.Room;
import com.example.petHotel.hotel.domain.RoomStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HotelCreateRequest {
    private UUID companyId;
    private String hotelName;
    private String hotelAddress;
    private String hotelPhone;
    private String hotelWebsite;
    private String hotelOwnerName;
    private String hotelProfileImg;
    private RoomRequest rooms;
    private ServiceRequest services;

    @Getter
    @Builder
    public static class RoomRequest {
        private String roomType;
        private Integer roomPrice;
        private RoomStatus roomStatus;
        private String roomDescription;
        private String dayCare;

        // RoomRequest -> Room 변환
        public Room toEntity() {
            return Room.builder()
                    .roomType(roomType)
                    .roomPrice(roomPrice)
                    .roomStatus(roomStatus)
                    .roomDescription(roomDescription)
                    .dayCare(dayCare)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ServiceRequest {
        private String serviceName;
        private String serviceDescription;
        private Integer servicePrice;
        private String serviceMemo;

        // ServiceRequest -> HotelService 변환
        public HotelService toEntity() {
            return HotelService.builder()
                    .serviceName(serviceName)
                    .serviceDescription(serviceDescription)
                    .servicePrice(servicePrice)
                    .serviceMemo(serviceMemo)
                    .build();
        }
    }

    // HotelCreateRequest -> HotelCreate 변환
    public HotelCreate to() {
        return HotelCreate.builder()
                .companyId(companyId)
                .hotelName(hotelName)
                .hotelAddress(hotelAddress)
                .hotelPhone(hotelPhone)
                .hotelWebsite(hotelWebsite)
                .hotelOwnerName(hotelOwnerName)
                .hotelProfileImg(hotelProfileImg)
                .rooms(rooms.toEntity())
                .services(services.toEntity())
                .build();
    }
}
