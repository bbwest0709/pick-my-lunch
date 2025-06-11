package com.pickmylunch.api.global.exception.advice;

import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    // 비즈니스 로직
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> globalExceptionHandler(BusinessLogicException e) {
        log.error("BusinessLogic Exception Error : {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getExceptionCode());
        return new ResponseEntity<>(errorResponse, e.getExceptionCode().getHttpStatus());
    }

    // 요청 바디 검증 실패
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage());
        return ErrorResponse.of(e.getBindingResult());
    }

    // 제약 조건 검증 실패
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException : {}", e.getMessage());
        return ErrorResponse.of(e.getConstraintViolations());
    }

    // HTTP 메서드 오류
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    // 잘못된 파라미터 타입
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException : {}", e.getMessage());
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 파일 업로드 에러
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(MissingServletRequestPartException e) {
        log.error("MissingServletRequestPartException : {}", e.getMessage());
        return ErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 요청 파라미터 누락
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException : {}", e.getMessage());
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
