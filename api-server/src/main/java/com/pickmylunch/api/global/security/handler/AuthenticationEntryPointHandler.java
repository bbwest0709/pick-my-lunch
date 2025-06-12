package com.pickmylunch.api.global.security.handler;

import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import com.pickmylunch.api.global.security.utils.ErrorResponseUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ErrorResponseUtils errorResponse;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AuthExceptionCode exceptionCode = getExceptionCodeByRequest(request);
        errorResponse.sendError(response, exceptionCode);
    }

    private AuthExceptionCode getExceptionCodeByRequest(HttpServletRequest request) {
        Exception e = (Exception) request.getAttribute("exception");
        if (e instanceof ExpiredJwtException) {
            return AuthExceptionCode.ACCESS_TOKEN_EXPIRED;
        } else if (e instanceof SignatureException) {
            return AuthExceptionCode.INVALID_SIGNATURE_ACCESS_TOKEN;
        } else if (e instanceof MalformedJwtException) {
            return AuthExceptionCode.MALFORMED_JWT_TOKEN;
        } else if (e instanceof UnsupportedJwtException) {
            return AuthExceptionCode.UNSUPPORTED_JWT_TOKEN;
        } else if (e instanceof IllegalArgumentException) {
            return AuthExceptionCode.EMPTY_JWT_CLAIMS;
        } else if (e instanceof JwtException) {
            return AuthExceptionCode.JWT_PROCESSING_ERROR;
        } else {
            return AuthExceptionCode.UNAUTHENTICATED;
        }
    }
}