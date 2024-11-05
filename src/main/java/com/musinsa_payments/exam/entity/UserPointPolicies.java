package com.musinsa_payments.exam.entity;

import com.musinsa_payments.exam.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_point_policies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointPolicies extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "max_points", nullable = false)
    private Integer maxPoints;

}
