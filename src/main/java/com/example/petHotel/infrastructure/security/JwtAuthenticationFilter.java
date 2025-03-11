package com.example.petHotel.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    private final JwtService jwtService;

    // 요청이 들어올 때마다 실행 되는 필터 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        TokenInfo tokenInfo = jwtService.parseToken(token);

        if(tokenInfo != null
                && !tokenInfo.getId().toString().isEmpty() // 토큰의 ID가 비어 있지 않으면 유효한 토큰으로 간주
                && SecurityContextHolder.getContext().getAuthentication() == null
        ){
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            tokenInfo,
                            null,
                            tokenInfo.getAuthorities()
                    );

            // 생성된 인증 정보를 SecurityContext에 설정하여 현재 사용자가 인증된 상태로 설정
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}