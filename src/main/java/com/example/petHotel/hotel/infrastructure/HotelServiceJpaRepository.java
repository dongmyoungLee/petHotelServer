package com.example.petHotel.hotel.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HotelServiceJpaRepository extends JpaRepository<HotelServiceEntity, UUID> {
}
