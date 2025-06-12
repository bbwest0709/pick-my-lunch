package com.pickmylunch.api.global.security.handler;

import com.pickmylunch.api.global.exception.code.AuthExceptionCode;
import com.pickmylunch.api.global.security.utils.ErrorResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationAccessDeniedHandler implements AccessDeniedHandler {

    private final ErrorResponseUtils errorResponse;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("[Access denied]: {}", request.getRequestURI());
        errorResponse.sendError(response, AuthExceptionCode.ACCESS_DENIED);
    }
}
