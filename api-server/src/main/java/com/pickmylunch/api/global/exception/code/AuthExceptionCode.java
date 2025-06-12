package com.pickmylunch.api.global.exception.code;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthExceptionCode implements ExceptionCode {
    // 인증 관련 오류
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access Token 만료"),
    INVALID_SIGNATURE_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "서명이 올바르지 않습니다."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증되지 않았습니다."),
    MALFORMED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 형식이 올바르지 않습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    EMPTY_JWT_CLAIMS(HttpStatus.UNAUTHORIZED, "JWT 클레임이 비어 있습니다."),
    JWT_PROCESSING_ERROR(HttpStatus.UNAUTHORIZED, "JWT 토큰 처리 중 오류가 발생했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다. 접근이 거부되었습니다."),
    
    // 권한 관련 오류
    AUTHENTICATION_FAILURE(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
