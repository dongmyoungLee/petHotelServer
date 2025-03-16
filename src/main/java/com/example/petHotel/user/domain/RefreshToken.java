package com.example.petHotel.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Getter
@Builder
public class RefreshToken {
    private Long id;
    private UUID userId;
    private String token;
}
