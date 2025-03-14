package com.example.petHotel.user.mock;

import com.example.petHotel.common.service.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {
    private final long millis;
    @Override
    public long millis() {
        return millis;
    }
}
