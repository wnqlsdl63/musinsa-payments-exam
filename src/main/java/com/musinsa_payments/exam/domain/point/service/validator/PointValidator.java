package com.musinsa_payments.exam.domain.point.service.validator;

import com.musinsa_payments.exam.common.exception.ValidationException;
import com.musinsa_payments.exam.domain.point.entity.Point;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import com.musinsa_payments.exam.domain.point.repository.PointDetailRepository;
import com.musinsa_payments.exam.domain.point.repository.PointRepository;
import com.musinsa_payments.exam.domain.policy.entity.UserPointPolicy;
import com.musinsa_payments.exam.domain.policy.repository.UserPointPolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.musinsa_payments.exam.common.constant.PointPolicyConstant.DEFAULT_USER_MAX_POINT;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointValidator {
    private final UserPointPolicyRepository userPointPoliciesRepository;
    private final PointRepository pointsRepository;
    private final PointDetailRepository pointDetailRepository;


    /**
     * 사용자의 포인트 적립 가능 여부를 검증합니다.
     *
     * - 해당 사용자의 최대 포인트 제한을 조회합니다. 사용자별 제한이 설정되어 있지 않으면 기본 포인트 제한값(20만)을 사용합니다.
     * - 현재 사용자가 보유하고 있는 포인트 총합에 적립하려는 금액을 더한 값이 최대 포인트 제한을 초과하는지 확인합니다.
     * - 만약 초과할 경우, ValidationException을 발생시켜 적립을 제한합니다.
     *
     * @param userId 포인트를 적립하려는 사용자 ID
     * @param amount 적립하려는 포인트 금액
     * @throws ValidationException 최대 보유 가능 포인트 한도를 초과하는 경우 예외 발생
     */
    public void validateAccumulatePoints(Long userId, long amount) {
        log.debug("[validateAccumulatePoints] userId: {}, amount: {}", userId, amount);
        // 사용자 최대 포인트 제한 조회
        Integer maxPointsLimit = userPointPoliciesRepository
                .findByUserId(userId)
                .map(UserPointPolicy::getMaxPoint)
                .orElse(DEFAULT_USER_MAX_POINT);

        // 현재 사용자 포인트 총합 가져와서 제한 확인
        int currentPoint = pointsRepository.getCurrentPointsByUserId(userId);
        log.debug("[validateAccumulatePoints] currentPoint: {}, maxPointsLimit: {}", currentPoint, maxPointsLimit);

        long newTotalPoint = currentPoint + amount;

        if (newTotalPoint > maxPointsLimit) {
            throw new ValidationException("최대 보유 가능한 포인트 한도를 초과할 수 없습니다.");
        }
    }

    /**
     * 적립 취소 가능 여부를 검증합니다.
     * 특정 적립 포인트가 사용된 적이 있는지 확인하여, 사용된 적이 있다면 적립 취소할 수 없도록 검증합니다.
     *
     * @param point 취소하려는 포인트
     * @throws ValidationException 적립된 포인트가 사용된 경우 예외 발생
     */
    public void validateCancelAccumulatePoint(Point point) {
        log.debug("[validateCancelAccumulatePoint] point id: {}, point status: {}", point.getId(), point.getStatus());

        if (!isCancellableStatus(point.getStatus())) {
            throw new ValidationException("사용자 적립, 수기로 적립된 포인트만 취소가 가능합니다.");
        }

        boolean isPointUsed = pointDetailRepository.existsByDetailPointIdAndStatus(point.getId(), PointStatus.USED);

        if (isPointUsed) {
            throw new ValidationException("적립된 포인트가 사용된 경우 취소할 수 없습니다.");
        }

    }

    /**
     * 사용자가 보유한 포인트가 사용하려는 포인트보다 충분한지 확인합니다.
     *
     * @param userId 포인트를 사용하려는 사용자 ID
     * @param amount 사용하려는 포인트 금액
     * @throws ValidationException 사용 가능한 포인트가 부족한 경우 예외 발생
     */
    public void validateSufficientPoints(Long userId, long amount) {
        log.debug("[validateSufficientPoints] userId: {}, amount: {}", userId, amount);

        // 현재 사용 가능한 포인트 조회
        final long availablePoint = pointsRepository.getCurrentPointsByUserId(userId);
        log.debug("[validateSufficientPoints] availablePoint: {}", availablePoint);

        // 사용하려는 금액과 비교
        if (availablePoint < amount) {
            throw new ValidationException("사용 가능한 포인트가 부족합니다.");
        }
    }

    /**
     * 적립 취소 가능한 상태값인지 확인합니다.
     * @param status
     * @return 취소 가능한 상태값인 경우 true, 그 외 false
     */
    private boolean isCancellableStatus(PointStatus status) {
        return status == PointStatus.ACCUMULATED || status == PointStatus.ADMIN_ACCUMULATED;
    }
}
