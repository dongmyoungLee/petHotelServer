package com.example.petHotel.hotel.service;

import com.example.petHotel.common.domain.exception.ResourceNotFoundException;
import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.common.service.ClockHolder;
import com.example.petHotel.hotel.contoller.request.request.HotelUpdateRequest;
import com.example.petHotel.hotel.domain.HotelResponse;
import com.example.petHotel.hotel.domain.*;
import com.example.petHotel.hotel.service.port.HotelRepository;
import com.example.petHotel.hotel.service.port.HotelServiceRepository;
import com.example.petHotel.hotel.service.port.RoomRepository;
import com.example.petHotel.user.domain.user.User;
import com.example.petHotel.user.domain.user.UserStatus;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
@Getter
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final HotelServiceRepository hotelServiceRepository;
    private final ClockHolder clockHolder;
    private final RoomService roomService;
    private final HotelSvcService hotelSvcService;
    private final JwtProvider jwtProvider;

    @Transactional
    public Hotel createHotel(HotelCreate hotelCreate) {
        // TODO 호텔추가 중복처리를 해야할까 .. ?

        // 호텔저장
        Hotel hotel = Hotel.from(hotelCreate, clockHolder);
        Hotel savedHotel = hotelRepository.save(hotel);

        // 객실 저장
        roomService.saveRooms(hotelCreate.getRooms(), savedHotel.getHotelId());

        // 호텔 서비스 저장
        hotelSvcService.saveServices(hotelCreate.getServices(), savedHotel.getHotelId());

        return hotel;
    }

    // 🚀 companyId로 Hotel 조회 (Room, Service 포함)
    @Transactional
    public List<HotelResponse> getHotelsByCompanyId(String token) {
        UUID companyId = jwtProvider.getUserIdFromRefreshToken(token);

        // 1️⃣ 호텔 리스트 조회
        List<Hotel> hotels = hotelRepository.findAllByCompanyId(companyId);

        if (hotels.isEmpty()) {
            return Collections.emptyList();
        }

        // 2️⃣ 호텔 ID 리스트 추출
        List<UUID> hotelIds = hotels.stream()
                .map(Hotel::getHotelId)
                .collect(Collectors.toList());

        // 3️⃣ 호텔 ID 기반으로 룸 & 서비스 조회
        Map<UUID, List<Room>> roomsByHotel = roomRepository.findByHotelIdIn(hotelIds)
                .stream()
                .collect(Collectors.groupingBy(Room::getHotelId));

        Map<UUID, List<HotelServiceDomain>> servicesByHotel = hotelServiceRepository.findByHotelIdIn(hotelIds)
                .stream()
                .collect(Collectors.groupingBy(HotelServiceDomain::getHotelId));

        // 4️⃣ 데이터 병합 (HotelResponse 리스트 생성)
        return hotels.stream()
                .map(hotel -> buildHotelResponse(hotel, roomsByHotel, servicesByHotel))
                .collect(Collectors.toList());
    }

    private HotelResponse buildHotelResponse(Hotel hotel, Map<UUID, List<Room>> roomsByHotel, Map<UUID, List<HotelServiceDomain>> servicesByHotel) {
        return HotelResponse.builder()
                .hotelId(hotel.getHotelId())
                .companyId(hotel.getCompanyId())
                .hotelName(hotel.getHotelName())
                .hotelAddress(hotel.getHotelAddress())
                .hotelPhone(hotel.getHotelPhone())
                .hotelWebsite(hotel.getHotelWebsite())
                .hotelOwnerName(hotel.getHotelOwnerName())
                .hotelProfileImg(hotel.getHotelProfileImg())
                .rooms(roomsByHotel.getOrDefault(hotel.getHotelId(), Collections.emptyList())
                        .stream().map(HotelResponse.RoomResponse::fromModel).collect(Collectors.toList()))
                .services(servicesByHotel.getOrDefault(hotel.getHotelId(), Collections.emptyList())
                        .stream().map(HotelResponse.ServiceResponse::fromModel).collect(Collectors.toList()))
                .build();
    }

    public UUID putHotelByCompanyId(HotelUpdateRequest hotelUpdateRequest) {
        Optional<Hotel> hotel = hotelRepository.findById(hotelUpdateRequest.getHotelId());

        if (hotel.isEmpty()) {
            throw new ResourceNotFoundException("hotel not found by companyID", hotelUpdateRequest.getCompanyId());
        }

        Hotel updateDomainEntity = hotel.get().updateHotel(hotelUpdateRequest);

        Hotel save = hotelRepository.save(updateDomainEntity);

        return save.getHotelId();
    }
}
