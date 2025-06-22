package com.pickmylunch.api.global.exception.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RatingExceptionCode implements ExceptionCode {

    ALREADY_RATE(HttpStatus.CONFLICT, "이미 음식점에 대한 평가를 진행했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
