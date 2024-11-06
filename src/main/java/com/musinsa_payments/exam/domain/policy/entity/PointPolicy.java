package com.musinsa_payments.exam.domain.policy.entity;

import com.musinsa_payments.exam.common.entity.BaseTimeEntity;
import com.musinsa_payments.exam.domain.policy.dto.PointPolicyDto;
import com.musinsa_payments.exam.domain.policy.dto.PolicyUpsertRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_policies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointPolicy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_key", nullable = false)
    private String settingKey;

    @Column(name = "setting_value", nullable = false)
    private String settingValue;

    private String description;

    @Builder(access = AccessLevel.PRIVATE)
    private PointPolicy(String settingKey, String settingValue, String description) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.description = description;
    }

    public static PointPolicy pointPolicy(PolicyUpsertRequestDto dto) {
        return PointPolicy.builder()
                .settingKey(dto.settingKey())
                .settingValue(dto.settingValue())
                .description(dto.description())
                .build();
    }

    public void updatePolicy(PolicyUpsertRequestDto dto) {
        this.settingValue = dto.settingValue();
        this.description = dto.description();
    }

    public PointPolicyDto toDto() {
        return new PointPolicyDto(id, settingKey, settingValue);
    }

}