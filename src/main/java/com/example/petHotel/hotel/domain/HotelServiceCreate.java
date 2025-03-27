package com.example.petHotel.hotel.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HotelServiceCreate {
    private String serviceName;
    private String serviceDescription;
    private Integer servicePrice;
    private String serviceMemo;
    private long hotel_serivce_RegistrationDate;
    private UUID hotelId;

}
