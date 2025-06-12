package com.pickmylunch.api.global.security.config;

import com.pickmylunch.api.global.redis.RedisRepository;
import com.pickmylunch.api.global.security.filter.JwtAuthenticationFilter;
import com.pickmylunch.api.global.security.filter.JwtVerificationFilter;
import com.pickmylunch.api.global.security.utils.ObjectMapperUtils;
import com.pickmylunch.api.global.security.utils.cookie.CookieUtils;
import com.pickmylunch.api.global.security.utils.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtFilterDsl extends AbstractHttpConfigurer<JwtFilterDsl, HttpSecurity> {

    private final ObjectMapperUtils objectMapper;
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;
    private final RedisRepository repository;

    public void configure(HttpSecurity builder) {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(objectMapper, jwtProvider, cookieUtils, repository);

        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtProvider);

        builder.addFilter(jwtAuthenticationFilter).addFilterBefore(jwtVerificationFilter, JwtAuthenticationFilter.class);
    }

    public JwtFilterDsl build() {
        return this;
    }
}
