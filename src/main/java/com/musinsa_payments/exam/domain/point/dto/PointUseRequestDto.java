package com.musinsa_payments.exam.domain.point.dto;

import com.musinsa_payments.exam.domain.point.annotation.MaxAccumulatePoint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PointUseRequestDto(
        @NotNull(message = "유저아이디는 필수값 입니다.")
        Long userId,

        @NotNull(message = "사용하려는 포인트 금액은 필수값 입니다.")
        @Min(value = 1, message = "사용하려는 포인트는 최소 1 이상이어야 합니다.")
        @MaxAccumulatePoint
        Long amount,

        @NotBlank(message = "주문번호는 필수값 입니다.")
        String orderId
) {
}
