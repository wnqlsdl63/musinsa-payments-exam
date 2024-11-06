package com.musinsa_payments.exam.domain.policy.service;

import com.musinsa_payments.exam.domain.policy.dto.PointPolicyDto;
import com.musinsa_payments.exam.domain.policy.dto.PolicyUpsertRequestDto;
import com.musinsa_payments.exam.domain.policy.dto.UserPointPolicyDto;
import com.musinsa_payments.exam.domain.policy.dto.UserPointPolicyUpsertRequestDto;
import com.musinsa_payments.exam.domain.policy.entity.PointPolicy;
import com.musinsa_payments.exam.domain.policy.entity.UserPointPolicy;
import com.musinsa_payments.exam.domain.policy.repository.PointPolicyRepository;
import com.musinsa_payments.exam.domain.policy.repository.UserPointPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PolicyService {

    private final PointPolicyRepository pointPolicyRepository;
    private final UserPointPolicyRepository userPointPolicyRepository;

    /**
     * 주어진 요청을 기반으로 포인트 정책을 생성하거나 업데이트 합니다.
     *
     * @param request 포인트 정책 upsert 요청 객체.
     * @return 생성되거나 업데이트된 포인트 정책.
     */
    public PointPolicyDto upsertPointPolicy(PolicyUpsertRequestDto request) {
        final PointPolicy pointPolicy = pointPolicyRepository.findBySettingKey(request.settingKey())
                .map(existingPolicy -> {
                    existingPolicy.updatePolicy(request);
                    return existingPolicy;
                })
                .orElseGet(() -> PointPolicy.createPointPolicy(request));

        return pointPolicyRepository.save(pointPolicy).toDto();
    }

    /**
     * 유저별 최대 보유 포인트 제한 데이터를 생성하거나 업데이트 합니다.
     *
     * @param request 유저별 포인트 정책 upsert 요청 객체.
     * @return 생성되거나 업데이트된 유저별 포인트 정책.
     */
    public UserPointPolicyDto upsertUserPointPolicy(UserPointPolicyUpsertRequestDto request) {
        final UserPointPolicy userPointPolicy = userPointPolicyRepository.findByUserId(request.userId())
                .map(existingPolicy -> {
                    existingPolicy.updateMaxPoint(request.maxPoint());
                    return existingPolicy;
                })
                .orElseGet(() -> UserPointPolicy.createUserPointPolicy(request.userId(), request.maxPoint()));

        return userPointPolicyRepository.save(userPointPolicy).toDto();
    }
}
