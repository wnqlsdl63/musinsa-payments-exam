package com.musinsa_payments.exam.domain.policy.dto;

public record UserPointPolicyDto(
        Long id,
        Long userId,
        Integer maxPoint
) {
}
