package com.example.petHotel.user.controller.Company;

import com.example.petHotel.user.controller.request.CompanyCreateRequest;
import com.example.petHotel.user.controller.response.CompanyCreateResponse;
import com.example.petHotel.user.domain.company.Company;
import com.example.petHotel.user.service.user.CompanyService;
import jakarta.mail.MessagingException;
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
    @PostMapping
    public ResponseEntity<CompanyCreateResponse> createCompany(@RequestBody CompanyCreateRequest companyCreateRequest) throws MessagingException {
        Company company = companyService.create(companyCreateRequest.to());

        return ResponseEntity.status(HttpStatus.CREATED).body(CompanyCreateResponse.from(company));
    }
}
