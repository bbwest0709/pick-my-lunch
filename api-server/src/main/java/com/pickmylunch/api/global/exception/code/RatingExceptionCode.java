package com.pickmylunch.api.global.exception.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum RatingExceptionCode implements ExceptionCode {

    ALREADY_RATE(HttpStatus.CONFLICT, "이미 음식점에 대한 평가를 진행했습니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "평가한 별점 정보를 찾을 수 없습니다."),
    MEMBER_NOT_SAME(HttpStatus.BAD_REQUEST, "작성자와 수정자가 같지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
