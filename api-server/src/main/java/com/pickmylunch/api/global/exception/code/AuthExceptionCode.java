package com.pickmylunch.api.global.exception.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthExceptionCode implements ExceptionCode {
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access Token 만료"),
    INVALID_SIGNATURE_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "서명이 올바르지 않습니다."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다.");

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
