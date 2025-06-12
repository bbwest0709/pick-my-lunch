package com.pickmylunch.api.global.security.utils;

import com.pickmylunch.api.global.exception.ErrorResponse;
import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ErrorResponseUtils {

    private final ObjectMapperUtils objectMapper;

    public void sendError(HttpServletResponse response, AuthExceptionCode authExceptionCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(authExceptionCode.getHttpStatus().value());
        response.getWriter().write(toErrorResponseString(authExceptionCode));
        response.getWriter().flush();
    }

    public String toErrorResponseString(AuthExceptionCode authExceptionCode) {
        ErrorResponse errorResponse = ErrorResponse.of(authExceptionCode.getHttpStatus(), authExceptionCode.getMessage());
        return objectMapper.toStringValue(errorResponse);
    }
}
