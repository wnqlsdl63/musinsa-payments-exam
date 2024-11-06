package com.musinsa_payments.exam.domain.point.entity;

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
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

    @Column(name = "target_point_id")
    private Long targetPointId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PointStatus status;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder(access = AccessLevel.PRIVATE)
    private PointDetail(Point point, Long targetPointId, Integer amount, PointStatus status) {
        this.point = point;
        this.targetPointId = targetPointId;
        this.amount = amount;
        this.status = status;
        this.createdDate = LocalDateTime.now();
    }

    public static PointDetail createPointDetail(Point point, Integer amount, PointStatus status) {
        return PointDetail.builder()
                .point(point)
                .targetPointId(point.getId())
                .amount(amount)
                .status(status)
                .build();
    }

}