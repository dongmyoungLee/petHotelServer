package com.example.petHotel.user.controller.company;

import com.example.petHotel.common.domain.service.JwtProvider;
import com.example.petHotel.user.controller.request.CompanyCreateRequest;
import com.example.petHotel.user.controller.request.UserLoginRequest;
import com.example.petHotel.user.controller.response.CompanyCreateResponse;
import com.example.petHotel.user.controller.response.LoginResponse;
import com.example.petHotel.user.domain.auth.UserToken;
import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.service.user.CompanyService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/companys")
@RequiredArgsConstructor
public class CompanyCreateController {
    private final CompanyService companyService;
    private final JwtProvider jwtProvider;
    @PostMapping
    public ResponseEntity<CompanyCreateResponse> createCompany(@RequestBody CompanyCreateRequest companyCreateRequest) throws MessagingException {
        Company company = companyService.create(companyCreateRequest.to());

        return ResponseEntity.status(HttpStatus.CREATED).body(CompanyCreateResponse.from(company));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> hotelLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {

        UserToken userToken = companyService.hotelLogin(userLoginRequest.getUserEmail(), userLoginRequest.getUserPwd());

        jwtProvider.addTokenToCookies(response, userToken.getAccessToken(), userToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(userToken));
    }
}
