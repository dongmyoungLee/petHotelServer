package com.example.petHotel.hotel.service;

import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.hotel.domain.*;
import com.example.petHotel.hotel.service.port.HotelRepository;
import com.example.petHotel.hotel.service.port.HotelServiceRepository;
import com.example.petHotel.hotel.service.port.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
@Getter
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelServiceRepository hotelServiceRepository;
    private final ClockHolder clockHolder;

    @Transactional
    public Hotel createHotel(HotelCreate hotelCreate) {
        // TODO 호텔추가 중복처리를 해야할까 .. ?

        // 호텔저장
        Hotel hotel = Hotel.from(hotelCreate, clockHolder);
        Hotel save = hotelRepository.save(hotel);

        // 룸 저장
        RoomCreate roomCreate = RoomCreate.builder()
                .roomType(hotel.getRooms().getRoomType())
                .roomPrice(hotel.getRooms().getRoomPrice())
                .roomStatus(hotel.getRooms().getRoomStatus())
                .roomDescription(hotel.getRooms().getRoomDescription())
                .dayCare(hotel.getRooms().getDayCare())
                .hotelId(save.getHotelId())
                .build();
        roomRepository.save(Room.from(roomCreate, clockHolder));

        // 호텔 서비스 저장
        HotelServiceCreate hotelServiceCreate = HotelServiceCreate.builder()
                .serviceName(hotel.getServices().getServiceName())
                .serviceDescription(hotel.getServices().getServiceDescription())
                .servicePrice(hotel.getServices().getServicePrice())
                .serviceMemo(hotel.getServices().getServiceMemo())
                .hotelId(save.getHotelId())
                .build();
        hotelServiceRepository.save(com.example.petHotel.hotel.domain.HotelService.from(hotelServiceCreate, clockHolder));


        return hotel;
    }
}
