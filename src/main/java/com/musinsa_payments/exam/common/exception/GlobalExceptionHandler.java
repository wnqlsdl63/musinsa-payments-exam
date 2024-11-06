package com.musinsa_payments.exam.common.exception;

import com.musinsa_payments.exam.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 어노테이션으로 유효성 검사를 통과하지 못한 예외를 처리합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().getFirst();
        String errorMessage = fieldError.getDefaultMessage();

        log.error("[handleMethodArgumentNotValidException] error: {}", errorMessage);
        return ApiResponse.exception(errorMessage);
    }

    /**
     * Custom Exception(ValidationException)을 처리합니다.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(ValidationException ex) {
        log.error("[handleValidationException] error: {}", ex.getMessage());
        return ApiResponse.exception(ex.getMessage());
    }

    /**
     * IllegalArgumentException을 처리합니다.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("[handleIllegalArgumentException] error: {}", ex.getMessage());
        return ApiResponse.exception(ex.getMessage());
    }


    /**
     * 시스템에서 Catch 하지 못한 예외를 처리합니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {
        log.error("[handleException] Internal server error: {}", ex.getMessage());
        return ApiResponse.error();
    }

}