package com.musinsa_payments.exam.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;


    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data));
    }

    public static ResponseEntity<ApiResponse<String>> exception(String message) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message, null));
    }

    public static ResponseEntity<ApiResponse<String>> error() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null));
    }
}