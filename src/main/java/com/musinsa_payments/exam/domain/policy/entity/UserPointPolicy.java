package com.musinsa_payments.exam.domain.policy.entity;

import com.musinsa_payments.exam.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_point_policies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointPolicy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "max_point", nullable = false)
    private Integer maxPoint;

}
