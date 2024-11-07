package com.musinsa_payments.exam.domain.point.dto;

import com.musinsa_payments.exam.domain.point.annotation.MaxAccumulatePoint;
import com.musinsa_payments.exam.domain.point.annotation.ValidExpireDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public record PointAccumulateRequestDto(
        @NotNull(message = "유저아이디는 필수값 입니다.")
        Long userId,

        @NotNull(message = "포인트 금액은 필수값 입니다.")
        @Min(value = 1, message = "적립하려는 포인트는 최소 1 이상이어야 합니다.")
        @MaxAccumulatePoint
        Long amount,

        String orderId,

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "만료일은 yyyy-MM-dd 형식이어야 합니다.")
        @ValidExpireDate
        String expireDate
) {
    // 입력된 expireDate값이 없다면 기본값 설정
    public LocalDateTime getExpireDateOrDefault() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // expireDate가 없으면 현재 날짜로부터 1년 뒤 23:59로 설정
        if (expireDate == null || expireDate.isBlank()) {
            return LocalDate.now().plusYears(1).atTime(23, 59);
        }

        // 입력된 expireDate가 있다면 해당 날짜의 23:59로 설정
        LocalDate parsedDate = LocalDate.parse(expireDate, formatter);
        return parsedDate.atTime(23, 59);
    }
}

