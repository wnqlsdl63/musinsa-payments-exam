package com.musinsa_payments.exam.domain.point.entity;

import com.musinsa_payments.exam.common.util.PointUtils;
import com.musinsa_payments.exam.domain.point.dto.PointAccumulateRequestDto;
import com.musinsa_payments.exam.domain.point.dto.PointDto;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PointStatus status;

    @Column(name = "expired_date")
    private LocalDateTime expireDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder(access = AccessLevel.PRIVATE)
    private Point(Long userId, Integer point, String orderId, PointStatus status, LocalDateTime expireDate) {
        this.userId = userId;
        this.point = point;
        this.orderId = orderId;
        this.status = status;
        this.expireDate = expireDate;
        this.createdDate = LocalDateTime.now();
    }

    public static Point createAccumulatePoint(PointAccumulateRequestDto dto, PointStatus status) {
        return Point.builder()
                .userId(dto.userId())
                .point(dto.amount())
                .orderId(dto.orderId())
                .status(status)
                .expireDate(dto.getExpireDateOrDefault())
                .build();
    }

    public static Point createAccumlateCanclePoint(Point point, PointStatus status) {
        return Point.builder()
                .userId(point.getUserId())
                .point(PointUtils.reverseSign(point.getPoint()))
                .orderId(point.getOrderId())
                .status(status)
                .build();
    }

    public PointDto toDto() {
        return PointDto.from(this);
    }

}
