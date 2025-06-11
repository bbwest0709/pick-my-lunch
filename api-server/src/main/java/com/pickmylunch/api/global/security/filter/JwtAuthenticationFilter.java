package com.pickmylunch.api.global.security.filter;

import com.pickmylunch.api.global.security.dto.LoginDto;
import com.pickmylunch.api.global.security.utils.ObjectMapperUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapperUtils objectMapper;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto requestData = objectMapper.toEntity(request, LoginDto.class);
        var authenticationToken = createAuthenticationToken(requestData);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginDto login) {
        return new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
    }
}
