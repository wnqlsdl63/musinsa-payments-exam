package com.musinsa_payments.exam.domain.point.entity;

import com.musinsa_payments.exam.common.util.PointUtils;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PointStatus status;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder(access = AccessLevel.PRIVATE)
    private PointDetail(Point point, Long detailPointId, Integer amount, PointStatus status) {
        this.point = point;
        this.detailPointId = detailPointId;
        this.amount = amount;
        this.status = status;
        this.createdDate = LocalDateTime.now();
    }

    public static PointDetail createAccumulatePointDetail(Point point, Integer amount, PointStatus status) {
        return PointDetail.builder()
                .point(point)
                .detailPointId(point.getId())
                .amount(amount)
                .status(status)
                .build();
    }

    public static PointDetail createAccumulateCacnelPointDetail(Point point, Point originPoint,PointStatus status) {
        return PointDetail.builder()
                .point(point)
                .detailPointId(originPoint.getId())
                .amount(PointUtils.reverseSign(originPoint.getPoint()))
                .status(status)
                .build();
    }

}