package com.example.petHotel.user.controller.model;

import com.example.petHotel.user.dto.UserDto;
import com.example.petHotel.user.service.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
// UserDto는 단순한 인터페이스라 강제적으로 implements한 클래스에서 오버라이드할 필요 없음.
// UserDtoImpl이 UserDto를 구현했지만, @Data 덕분에 getter가 자동 생성되므로 오버라이드 없이도 사용 가능.
public class UserDtoImpl implements UserDto {
    private UUID id;
    private String userEmail;
    private String userPwd;
    private String userName;
    private String userPhone;
    private String userAddr;
    private LocalDateTime userRegistrationDate;
    private Role role;
}