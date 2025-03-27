package com.example.petHotel.hotel.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class HotelResponse {
    private UUID hotelId;
    private UUID companyId;
    private String hotelName;
    private String hotelAddress;
    private String hotelPhone;
    private String hotelWebsite;
    private String hotelOwnerName;
    private String hotelProfileImg;
    private List<RoomResponse> rooms;
    private List<ServiceResponse> services;


    @Getter
    @Builder
    public static class RoomResponse {
        private UUID roomId;
        private String roomType;
        private int roomPrice;
        private RoomStatus roomStatus;
        private String roomDescription;
        private String dayCare;
        private Long roomRegistrationDate;

        public static RoomResponse fromModel(Room room) {
            return RoomResponse.builder()
                    .roomId(room.getRoomId())
                    .roomType(room.getRoomType())
                    .roomPrice(room.getRoomPrice())
                    .roomStatus(room.getRoomStatus())
                    .roomDescription(room.getRoomDescription())
                    .dayCare(room.getDayCare())
                    .roomRegistrationDate(room.getRoomRegistrationDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ServiceResponse {
        private UUID serviceId;
        private String serviceName;
        private String serviceDescription;
        private int servicePrice;
        private String serviceMemo;

        public static ServiceResponse fromModel(HotelServiceDomain service) {
            return ServiceResponse.builder()
                    .serviceId(service.getServiceId())
                    .serviceName(service.getServiceName())
                    .serviceDescription(service.getServiceDescription())
                    .servicePrice(service.getServicePrice())
                    .serviceMemo(service.getServiceMemo())
                    .build();
        }
    }
}
