package com.musinsa_payments.exam.domain.point.dto;

import com.musinsa_payments.exam.domain.point.entity.Point;

import java.time.format.DateTimeFormatter;

public record PointDto(
        Long id,
        Long userId,
        String orderId,
        Long point,
        String statusDesc,
        String expireDate,
        String createdDate
) {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static PointDto from(Point point) {
        return new PointDto(
                point.getId(),
                point.getUserId(),
                point.getOrderId(),
                point.getPoint(),
                point.getStatus().getDescription(),
                point.getExpireDate() != null ? point.getExpireDate().format(formatter) : null,
                point.getCreatedDate() != null ? point.getCreatedDate().format(formatter) : null
        );
    }
}

