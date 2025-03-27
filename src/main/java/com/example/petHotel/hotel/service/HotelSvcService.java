package com.example.petHotel.hotel.service;

import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.hotel.domain.HotelServiceDomain;
import com.example.petHotel.hotel.service.port.HotelServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelSvcService {
    private final HotelServiceRepository hotelServiceRepository;
    private final ClockHolder clockHolder;

    @Transactional
    public void saveServices(List<HotelServiceDomain> services, UUID hotelId) {

        List<HotelServiceDomain> serviceEntities = services.stream()
                .map(service -> HotelServiceDomain.builder()
                        .serviceName(service.getServiceName())
                        .serviceDescription(service.getServiceDescription())
                        .servicePrice(service.getServicePrice())
                        .serviceMemo(service.getServiceMemo())
                        .hotelId(hotelId)
                        .hotel_service_RegistrationDate(clockHolder.millis())
                        .build())
                .toList();

        hotelServiceRepository.saveAll(serviceEntities);
    }
}
