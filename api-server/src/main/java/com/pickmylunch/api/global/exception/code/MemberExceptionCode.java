package com.pickmylunch.api.global.exception.code;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberExceptionCode implements ExceptionCode {
    DUPLICATED_MEMBER_NAME(HttpStatus.CONFLICT, "이미 사용 중인 계정입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    REALTIME_LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "회원의 실시간 위치 정보를 찾을 수 없습니다."),
    LOCATION_NOT_FOUNT(HttpStatus.NOT_FOUND, "회원의 위치 정보를 찾을 수 없습니다."),
    DEFAULT_LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "기본 위치 정보를 찾을 수 없습니다."),
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
