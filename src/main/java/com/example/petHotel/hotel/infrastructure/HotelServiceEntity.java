package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.HotelServiceDomain;
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
    private long hotel_service_RegistrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    public HotelServiceDomain toModel() {
        return HotelServiceDomain.builder()
                .serviceId(serviceId)
                .serviceName(serviceName)
                .serviceDescription(serviceDescription)
                .servicePrice(servicePrice)
                .serviceMemo(serviceMemo)
                .hotel_service_RegistrationDate(hotel_service_RegistrationDate)
                .hotelId(hotel.getHotelId())
                .build();
    }

    public static HotelServiceEntity fromModel(HotelServiceDomain hotelServiceDomain) {
        HotelServiceEntity hotelServiceEntity = new HotelServiceEntity();
        hotelServiceEntity.serviceId = hotelServiceDomain.getServiceId();
        hotelServiceEntity.serviceName = hotelServiceDomain.getServiceName();
        hotelServiceEntity.serviceDescription = hotelServiceDomain.getServiceDescription();
        hotelServiceEntity.servicePrice = hotelServiceDomain.getServicePrice();
        hotelServiceEntity.serviceMemo = hotelServiceDomain.getServiceMemo();
        hotelServiceEntity.hotel_service_RegistrationDate = hotelServiceDomain.getHotel_service_RegistrationDate();
        hotelServiceEntity.hotel = HotelEntity.builder().hotelId(hotelServiceDomain.getHotelId()).build();
        return hotelServiceEntity;
    }
}
