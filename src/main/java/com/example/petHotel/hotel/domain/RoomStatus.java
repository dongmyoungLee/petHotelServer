package com.example.petHotel.hotel.domain;

public enum RoomStatus {
    AVAILABLE, RESERVED, OCCUPIED, MAINTENANCE

    /*
    *
    * RESERVED: 예약된 상태 (사용자가 예약했지만 아직 체크인하지 않은 상태)

    AVAILABLE: 사용 가능한 상태 (빈 방, 예약되지 않은 상태)

    MAINTENANCE: 점검 중 상태 (청소 또는 수리가 진행 중인 상태)

    OCCUPIED: 투숙 중 상태 (현재 사용자가 체크인하여 방을 사용하는 상태)


    *
    * */
}
