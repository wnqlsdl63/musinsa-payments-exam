package com.musinsa_payments.exam.domain.policy.repository;

import com.musinsa_payments.exam.domain.policy.entity.PointPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {
    Optional<PointPolicy> findBySettingKey(String settingKey);
}
