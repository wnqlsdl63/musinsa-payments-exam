package com.musinsa_payments.exam.domain.point.entity;

import com.musinsa_payments.exam.common.util.PointUtils;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_point_id", nullable = false)
    private Point point;

    @Column(name = "detail_point_id")
    private Long detailPointId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PointStatus status;

    @Column(name = "expired_date")
    private LocalDateTime expireDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder(access = AccessLevel.PRIVATE)
    private PointDetail(Point point, Long detailPointId, Long amount, Long userId, LocalDateTime expireDate, PointStatus status) {
        this.point = point;
        this.detailPointId = detailPointId;
        this.amount = amount;
        this.userId = userId;
        this.status = status;
        this.expireDate = expireDate;
    }

    public static PointDetail createAccumulatePointDetail(Point point, Long amount, PointStatus status) {
        return PointDetail.builder()
                .point(point)
                .detailPointId(point.getId())
                .userId(point.getUserId())
                .amount(amount)
                .status(status)
                .expireDate(point.getExpireDate())
                .build();
    }

    public static PointDetail createAccumulateCacnelPointDetail(Point point, Point originPoint, PointStatus status) {
        return PointDetail.builder()
                .point(point)
                .detailPointId(originPoint.getId())
                .userId(point.getUserId())
                .amount(PointUtils.reverseSign(originPoint.getPoint()))
                .status(status)
                .build();
    }

    public static PointDetail createUsePointDetail(Point point, Long detailPointId, Long amount) {
        return PointDetail.builder()
                .point(point)
                .userId(point.getUserId())
                .detailPointId(detailPointId)
                .amount(PointUtils.reverseSign(amount))
                .status(PointStatus.USED)
                .build();
    }

}
