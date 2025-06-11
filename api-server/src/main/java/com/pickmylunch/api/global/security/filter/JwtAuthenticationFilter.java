package com.pickmylunch.api.global.security.filter;

import com.pickmylunch.api.global.security.details.AuthUser;
import com.pickmylunch.api.global.security.dto.LoginDto;
import com.pickmylunch.api.global.security.utils.ObjectMapperUtils;
import com.pickmylunch.api.global.security.utils.cookie.CookieUtils;
import com.pickmylunch.api.global.security.utils.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Value("${jwt.prefix}")
    private String prefix;

    private final ObjectMapperUtils objectMapper;
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto requestData = objectMapper.toEntity(request, LoginDto.class);
        var authenticationToken = createAuthenticationToken(requestData);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        AuthUser authUser = (AuthUser) authResult.getPrincipal();

        String accessToken = createAccessToken(authUser);
        String refreshToken = createRefreshToken(authUser);

        response.setHeader(HttpHeaders.AUTHORIZATION, prefix + accessToken);
        response.addCookie(cookieUtils.createCookie(refreshToken));
    }


    private UsernamePasswordAuthenticationToken createAuthenticationToken(LoginDto login) {
        return new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
    }

    private String createAccessToken(AuthUser authUser) {
        return jwtProvider.generateAccessToken(
                authUser.getUsername(), authUser.getId(), toTrans(authUser.getAuthorities())
        );
    }

    private String createRefreshToken(AuthUser authUser) {
        return jwtProvider.generateRefreshToken(authUser.getUsername());
    }

    private String toTrans(Collection<GrantedAuthority> list) {
        return StringUtils.collectionToCommaDelimitedString(list);
    }
}
