package com.example.petHotel.hotel.domain;

import com.example.petHotel.common.service.ClockHolder;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HotelService {
    private UUID serviceId;
    private String serviceName;
    private String serviceDescription;
    private Integer servicePrice;
    private String serviceMemo;
    private long hotel_service_RegistrationDate;
    private UUID hotelId;

    public static HotelService from(HotelServiceCreate hotelServiceCreate, ClockHolder clockHolder) {
        return HotelService.builder()
                .serviceName(hotelServiceCreate.getServiceName())
                .serviceDescription(hotelServiceCreate.getServiceDescription())
                .servicePrice(hotelServiceCreate.getServicePrice())
                .serviceMemo(hotelServiceCreate.getServiceMemo())
                .hotel_service_RegistrationDate(clockHolder.millis())
                .hotelId(hotelServiceCreate.getHotelId())
                .build();
    }
}
