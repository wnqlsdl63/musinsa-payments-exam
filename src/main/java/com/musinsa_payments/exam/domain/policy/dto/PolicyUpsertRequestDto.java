package com.musinsa_payments.exam.domain.policy.dto;

import jakarta.validation.constraints.NotBlank;

public record PolicyUpsertRequestDto(
        @NotBlank(message = "settingKey는 필수값 입니다.")
        String settingKey,

        @NotBlank(message = "settingValue는 필수값 입니다.")
        String settingValue,

        String description
) {
}
