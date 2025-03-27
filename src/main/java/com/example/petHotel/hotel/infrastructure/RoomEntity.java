package com.example.petHotel.hotel.infrastructure;

import com.example.petHotel.hotel.domain.Hotel;
import com.example.petHotel.hotel.domain.Room;
import com.example.petHotel.hotel.domain.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "room_id", nullable = false, updatable = false)
    private UUID roomId;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_price", nullable = false)
    private Integer roomPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status", nullable = false)
    private RoomStatus roomStatus;

    @Column(name = "room_description")
    private String roomDescription;

    @Column(name = "day_care")
    private String dayCare;

    @Column(name = "room_registration_date", nullable = false)
    private long roomRegistrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    public Room toModel() {
        return Room.builder()
                .roomId(roomId)
                .roomType(roomType)
                .roomPrice(roomPrice)
                .roomStatus(roomStatus)
                .roomDescription(roomDescription)
                .dayCare(dayCare)
                .roomRegistrationDate(roomRegistrationDate)
                .hotelId(hotel.getHotelId())
                .build();
    }

    public static RoomEntity fromModel(Room room) {
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.roomId = room.getRoomId();
        roomEntity.roomType = room.getRoomType();
        roomEntity.roomPrice = room.getRoomPrice();
        roomEntity.roomStatus = room.getRoomStatus();
        roomEntity.roomDescription = room.getRoomDescription();
        roomEntity.dayCare = room.getDayCare();
        roomEntity.roomRegistrationDate = room.getRoomRegistrationDate();
        roomEntity.hotel = HotelEntity.builder().hotelId(room.getHotelId()).build();
        return roomEntity;
    }

}
