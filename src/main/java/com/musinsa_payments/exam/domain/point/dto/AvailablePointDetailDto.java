package com.musinsa_payments.exam.domain.point.dto;

import com.musinsa_payments.exam.domain.point.enums.PointStatus;

import java.time.LocalDateTime;

public record AvailablePointDetailDto(
        Long detailPointId,
        Long sumAmount,
        PointStatus status,
        LocalDateTime expireDate
) {
}
