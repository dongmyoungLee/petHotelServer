package com.example.petHotel.infrastructure.security;

import com.example.petHotel.common.domain.dto.TokenInfo;
import com.example.petHotel.common.domain.exception.UnauthorizedException;
import com.example.petHotel.common.domain.service.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvier;

    // 요청이 들어올 때마다 실행 되는 필터 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("쿠키 목록:");

        try {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("쿠키 이름: " + cookie.getName() + ", 값: " + cookie.getValue());
            }

        } catch (Exception e) {

        }


        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            TokenInfo tokenInfo = jwtProvier.parseToken(token);

            if (tokenInfo != null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        tokenInfo,
                        null,
                        tokenInfo.getAuthorities()
                );
                // 생성된 인증 정보를 SecurityContext에 설정하여 현재 사용자가 인증된 상태로 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (UnauthorizedException e) {
            // UnauthorizedException이 발생하면 401로 응답 ..
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}