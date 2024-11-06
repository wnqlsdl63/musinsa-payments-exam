package com.musinsa_payments.exam.domain.policy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserPointPolicyUpsertRequestDto(
        @NotNull(message = "userId는 필수값입니다.")
        Long userId,

        @NotNull(message = "maxPoint는 필수값입니다.")
        @Min(value = 0, message = "최대 포인트는 0 이상이어야 합니다.")
        Integer maxPoint
) {
}
