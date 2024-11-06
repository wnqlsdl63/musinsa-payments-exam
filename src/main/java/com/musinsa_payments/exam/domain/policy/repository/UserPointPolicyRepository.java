package com.musinsa_payments.exam.domain.policy.repository;

import com.musinsa_payments.exam.domain.policy.entity.UserPointPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPointPolicyRepository extends JpaRepository<UserPointPolicy, Long> {
    Optional<UserPointPolicy> findByUserId(Long userId);
}
