package com.musinsa_payments.exam.domain.point.enums;

import lombok.Getter;

@Getter
public enum PointStatus {
    ACCUMULATED("적립"),
    ADMIN_ACCUMULATED("관리자 수기 적립"),
    ACCUMULATION_CANCELLED("적립취소"),
    USED("사용"),
    USE_CANCELLED("사용취소"),
    EXPIRED("만료");

    private final String description;

    PointStatus(String description) {
        this.description = description;
    }
}
