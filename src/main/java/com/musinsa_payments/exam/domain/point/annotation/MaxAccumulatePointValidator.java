package com.musinsa_payments.exam.domain.point.annotation;

import com.musinsa_payments.exam.domain.policy.entity.PointPolicy;
import com.musinsa_payments.exam.domain.policy.repository.PointPolicyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaxAccumulatePointValidator implements ConstraintValidator<MaxAccumulatePoint, Integer> {

    private final PointPolicyRepository pointPolicyRepository;

    private static final String MAX_ACCUMULATE_POINT_KEY = "MAX_ACCUMULATE_POINT";
    private static final int DEFAULT_MAX_ACCUMULATE_POINT = 100000; // 기본값 10만 포인트

    @Override
    public boolean isValid(Integer amount, ConstraintValidatorContext context) {
        if (amount == null) {
            return false;
        }

        // DB에서 최대 적립 가능 포인트 조회
        int maxAccumulatePoint = pointPolicyRepository.findBySettingKey(MAX_ACCUMULATE_POINT_KEY)
                .map(policy -> Integer.parseInt(policy.getSettingValue()))
                .orElse(DEFAULT_MAX_ACCUMULATE_POINT);

        // amount가 최대 적립 포인트 이하인지 검증
        boolean isValid = amount <= maxAccumulatePoint;

        // 유효하지 않다면 사용자 정의 메시지를 설정
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("포인트 1회 적립시 %d 이상 적립할 수 없습니다.", maxAccumulatePoint)
            ).addConstraintViolation();
        }

        // amount가 최대 적립 포인트 이하인지 검증
        return isValid;
    }
}
