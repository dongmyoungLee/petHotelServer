package com.example.petHotel.user.infrastructure.auth;

import com.example.petHotel.user.domain.auth.RefreshToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    public RefreshToken toModel() {
        return RefreshToken.builder()
                .id(id)
                .userId(userId)
                .token(token)
                .build();
    }

    public static RefreshTokenEntity fromModel(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.id = refreshToken.getId();
        refreshTokenEntity.userId = refreshToken.getUserId();
        refreshTokenEntity.token = refreshToken.getToken();
        return refreshTokenEntity;
    }
}