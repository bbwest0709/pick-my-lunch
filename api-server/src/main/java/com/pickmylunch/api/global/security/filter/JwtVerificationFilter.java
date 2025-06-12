package com.pickmylunch.api.global.security.filter;

import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import com.pickmylunch.api.global.security.details.AuthUser;
import com.pickmylunch.api.global.security.utils.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            setAuthenticationToContext(request);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToContext(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(createAuthenticatiedToken(request));
    }

    private Authentication createAuthenticatiedToken(HttpServletRequest request) {
        AuthUser authUser = createUserDetail(request);
        return new UsernamePasswordAuthenticationToken(authUser.getId(), null, authUser.getAuthorities());
    }

    private AuthUser createUserDetail(HttpServletRequest request) {
        String token = getAuthenticationTokenToHeader(request).substring(jwtProvider.getPrefix().length());
        return new AuthUser(jwtProvider.getClaims(token));
    }

    private String getAuthenticationTokenToHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
