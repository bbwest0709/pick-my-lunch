package com.pickmylunch.api.global.exception.code;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {
    HttpStatus getHttpStatus();
    String getMessage();
}
