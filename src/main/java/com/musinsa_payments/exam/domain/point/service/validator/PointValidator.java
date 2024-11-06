package com.musinsa_payments.exam.domain.point.service.validator;

import com.musinsa_payments.exam.common.constant.PointPolicyConstant;
import com.musinsa_payments.exam.common.exception.ValidationException;
import com.musinsa_payments.exam.domain.point.repository.PointRepository;
import com.musinsa_payments.exam.domain.policy.entity.UserPointPolicy;
import com.musinsa_payments.exam.domain.policy.repository.UserPointPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.musinsa_payments.exam.common.constant.PointPolicyConstant.DEFAULT_USER_MAX_POINT;

@Component
@RequiredArgsConstructor
public class PointValidator {

    private final UserPointPolicyRepository userPointPoliciesRepository;
    private final PointRepository pointsRepository;

    public void validateAccumulatePoints(Long userId, int amount) {
        // 사용자 최대 포인트 제한 조회
        Integer maxPointsLimit = userPointPoliciesRepository
                .findByUserId(userId)
                .map(UserPointPolicy::getMaxPoint)
                .orElse(DEFAULT_USER_MAX_POINT);

        // 현재 사용자 포인트 총합 가져와서 제한 확인
        int currentPoints = pointsRepository.getCurrentPoints(userId);
        int newTotalPoints = currentPoints + amount;

        if (newTotalPoints > maxPointsLimit) {
            throw new ValidationException("최대 보유 가능한 포인트 한도를 초과할 수 없습니다.");
        }
    }
}
