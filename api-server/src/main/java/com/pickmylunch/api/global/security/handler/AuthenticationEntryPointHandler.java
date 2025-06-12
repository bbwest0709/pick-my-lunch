package com.pickmylunch.api.global.security.handler;

import com.pickmylunch.api.global.exception.ErrorResponse;
import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import com.pickmylunch.api.global.security.utils.ObjectMapperUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapperUtils objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AuthExceptionCode exceptionCode = getExceptionCodeByRequest(request);
        sendError(response, exceptionCode);
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

    private void sendError(HttpServletResponse response, AuthExceptionCode authExceptionCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(authExceptionCode.getHttpStatus().value());
        response.getWriter().write(getResponseData(authExceptionCode));
    }

    private String getResponseData(AuthExceptionCode authExceptionCode) {
        return objectMapper.toStringValue(createErrorResponse(authExceptionCode));
    }

    private ErrorResponse createErrorResponse(AuthExceptionCode authExceptionCode) {
        return ErrorResponse.of(authExceptionCode.getHttpStatus(), authExceptionCode.getMessage());
    }
}