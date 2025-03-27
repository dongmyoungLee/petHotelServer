package com.example.petHotel.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain
        securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable);
        security.sessionManagement(
                configurer -> configurer
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS));
        security.authenticationProvider(authenticationProvider);
        security.addFilterBefore(jwtAuthenticationFilter
                , UsernamePasswordAuthenticationFilter.class);
        security.authorizeHttpRequests(req ->
                req.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/users"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/companys"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/companys/login"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/auth"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/api/v1/auth/kakao"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/api/v1/auth/google"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/api/v1/auth/naver"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/auth/logout"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/auth/validToken"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/auth/refresh"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/hotels"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET,"/api/v1/hotels/**"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/users/**/verify")
                )
                        .permitAll()
                        // 나중에 ADMIN 사용할때.. 테스트 양식
                        .requestMatchers("/api/v1/auth/test2").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );
        security.cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정 추가
        return security.build();

    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cookie"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Set-Cookie"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



}