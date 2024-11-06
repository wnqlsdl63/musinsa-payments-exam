package com.musinsa_payments.exam.domain.policy.entity;

import com.musinsa_payments.exam.common.entity.BaseTimeEntity;
import com.musinsa_payments.exam.domain.policy.dto.UserPointPolicyDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder(access = AccessLevel.PRIVATE)
    private UserPointPolicy(Long userId, Integer maxPoint) {
        this.userId = userId;
        this.maxPoint = maxPoint;
    }

    public static UserPointPolicy createUserPointPolicy(Long userId, Integer maxPoint) {
        return UserPointPolicy.builder()
                .userId(userId)
                .maxPoint(maxPoint)
                .build();
    }

    public void updateMaxPoint(Integer maxPoint) {
        this.maxPoint = maxPoint;
    }

    public UserPointPolicyDto toDto() {
        return new UserPointPolicyDto(id, userId, maxPoint);
    }
}
