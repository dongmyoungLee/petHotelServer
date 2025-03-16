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
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/auth"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/auth/refresh"),
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/users/**/verify")
                )
                        .permitAll()
                        // 나중에 ADMIN 사용할때.. 테스트 양식
                        .requestMatchers("/api/v1/auth/test2").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );
        return security.build();

    }


}