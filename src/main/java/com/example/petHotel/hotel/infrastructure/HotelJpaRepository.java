package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HotelJpaRepository extends JpaRepository<HotelEntity, UUID> {
    Optional<HotelEntity> findByCompanyId(UUID companyId);
}
