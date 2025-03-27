package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Hotel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "hotels")
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hotel_id", nullable = false)
    private UUID hotelId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @Column(name = "hotel_address")
    private String hotelAddress;

    @Column(name = "hotel_phone")
    private String hotelPhone;

    @Column(name = "hotel_website")
    private String hotelWebsite;

    @Column(name = "hotel_owner_name", nullable = false)
    private String hotelOwnerName;

    @Column(name = "hotel_profile_img")
    private String hotelProfileImg;

    @Column(name = "hotel_registration_date", nullable = false)
    private long hotelRegistrationDate;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HotelServiceEntity> services;

    public Hotel toModel() {
        return Hotel.builder()
                .hotelId(hotelId)
                .companyId(companyId)
                .hotelName(hotelName)
                .hotelAddress(hotelAddress)
                .hotelPhone(hotelPhone)
                .hotelWebsite(hotelWebsite)
                .hotelOwnerName(hotelOwnerName)
                .hotelProfileImg(hotelProfileImg)
                .hotelRegistrationDate(hotelRegistrationDate)
                .build();
    }

    public static HotelEntity fromModel(Hotel hotel) {
        return HotelEntity.builder()
                .hotelId(hotel.getHotelId())
                .companyId(hotel.getCompanyId())
                .hotelName(hotel.getHotelName())
                .hotelAddress(hotel.getHotelAddress())
                .hotelPhone(hotel.getHotelPhone())
                .hotelWebsite(hotel.getHotelWebsite())
                .hotelOwnerName(hotel.getHotelOwnerName())
                .hotelProfileImg(hotel.getHotelProfileImg())
                .hotelRegistrationDate(hotel.getHotelRegistrationDate())
                .build();
    }

}
