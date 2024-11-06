package com.musinsa_payments.exam.domain.policy.service;

import com.musinsa_payments.exam.domain.policy.dto.PointPolicyDto;
import com.musinsa_payments.exam.domain.policy.dto.PolicyUpsertRequestDto;
import com.musinsa_payments.exam.domain.policy.entity.PointPolicy;
import com.musinsa_payments.exam.domain.policy.repository.PointPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PolicyService {

    private final PointPolicyRepository pointPolicyRepository;

    /**
     * 주어진 요청을 기반으로 포인트 정책을 생성하거나 업데이트합니다.
     *
     * @param request 포인트 정책 upsert 요청 객체.
     * @return 생성되거나 업데이트된 포인트 정.
     */
    public PointPolicyDto upsertPointPolicy(PolicyUpsertRequestDto request) {
        final PointPolicy pointPolicy = pointPolicyRepository.findBySettingKey(request.settingKey())
                .map(existingPolicy -> {
                    existingPolicy.updatePolicy(request);
                    return existingPolicy;
                })
                .orElseGet(() -> PointPolicy.pointPolicy(request));

        return pointPolicyRepository.save(pointPolicy).toDto();
    }
}
