package com.example.petHotel.user.controller.model;

import com.example.petHotel.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// UserDto는 단순한 인터페이스라 강제적으로 implements한 클래스에서 오버라이드할 필요 없음.
// UserDtoImpl이 UserDto를 구현했지만, @Data 덕분에 getter가 자동 생성되므로 오버라이드 없이도 사용 가능.
public class UserDtoImpl implements UserDto {
    private Long id;
    private String name;
    private String email;
}