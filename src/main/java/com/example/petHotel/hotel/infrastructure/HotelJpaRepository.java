package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HotelJpaRepository extends JpaRepository<HotelEntity, UUID> {
    List<HotelEntity> findAllByCompanyId(UUID companyId);
}
