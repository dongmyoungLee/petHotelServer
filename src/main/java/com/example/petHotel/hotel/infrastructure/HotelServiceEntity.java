package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Hotel;
import com.example.petHotel.hotel.domain.HotelService;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "hotel_service")
public class HotelServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "service_id", nullable = false, updatable = false)
    private UUID serviceId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_description", nullable = false)
    private String serviceDescription;

    @Column(name = "service_price", nullable = false)
    private Integer servicePrice;

    @Column(name = "service_memo")
    private String serviceMemo;

    @Column(name = "hotel_service_registration_date", nullable = false)
    private long hotel_serivce_RegistrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    public HotelService toModel() {
        return HotelService.builder()
                .serviceId(serviceId)
                .serviceName(serviceName)
                .serviceDescription(serviceDescription)
                .servicePrice(servicePrice)
                .serviceMemo(serviceMemo)
                .hotel_service_RegistrationDate(hotel_serivce_RegistrationDate)
                .hotelId(hotel.getHotelId())
                .build();
    }

    public static HotelServiceEntity fromModel(HotelService hotelService) {
        HotelServiceEntity hotelServiceEntity = new HotelServiceEntity();
        hotelServiceEntity.serviceId = hotelService.getServiceId();
        hotelServiceEntity.serviceName = hotelService.getServiceName();
        hotelServiceEntity.serviceDescription = hotelService.getServiceDescription();
        hotelServiceEntity.servicePrice = hotelService.getServicePrice();
        hotelServiceEntity.serviceMemo = hotelService.getServiceMemo();
        hotelServiceEntity.hotel_serivce_RegistrationDate = hotelService.getHotel_service_RegistrationDate();
        hotelServiceEntity.hotel = HotelEntity.builder().hotelId(hotelService.getHotelId()).build();
        return hotelServiceEntity;
    }
}
