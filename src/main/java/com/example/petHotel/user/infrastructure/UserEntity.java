package com.example.petHotel.user.infrastructure;

import com.example.petHotel.user.domain.Role;
import com.example.petHotel.user.domain.SnsType;
import com.example.petHotel.user.domain.User;
import com.example.petHotel.user.domain.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_pwd")
    private String userPwd;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_addr")
    private String userAddr;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "certification_code")
    private String certificationCode;

    @Column(name = "user_registration_date", nullable = false)
    private long userRegistrationDate;

    @Column(name = "sns_type")
    @Enumerated(EnumType.STRING)
    private SnsType snsType;

    public User toModel() {
        return User.builder()
                .userId(userId)
                .userEmail(userEmail)
                .userPwd(userPwd)
                .userName(userName)
                .userPhone(userPhone)
                .userAddr(userAddr)
                .status(status)
                .role(role)
                .snsType(snsType)
                .certificationCode(certificationCode)
                .userRegistrationDate(userRegistrationDate)
                .build();
    }

    public static UserEntity fromModel(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.userId = user.getUserId();
        userEntity.userEmail = user.getUserEmail();
        userEntity.userPwd = user.getUserPwd();
        userEntity.userName = user.getUserName();
        userEntity.userPhone = user.getUserPhone();
        userEntity.userAddr = user.getUserAddr();
        userEntity.status = user.getStatus();
        userEntity.snsType = user.getSnsType();
        userEntity.role = user.getRole();
        userEntity.certificationCode = user.getCertificationCode();
        userEntity.userRegistrationDate = user.getUserRegistrationDate();
        return userEntity;
    }
}
