package com.musinsa_payments.exam.domain.point.annotation;

import com.musinsa_payments.exam.domain.policy.entity.PointPolicy;
import com.musinsa_payments.exam.domain.policy.repository.PointPolicyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.musinsa_payments.exam.common.constant.PointPolicyConstant.DEFAULT_MAX_ACCUMULATE_POINT;
import static com.musinsa_payments.exam.common.constant.PointPolicyConstant.MAX_ACCUMULATE_POINT_KEY;

@Component
@RequiredArgsConstructor
public class MaxAccumulatePointValidator implements ConstraintValidator<MaxAccumulatePoint, Integer> {

    private final PointPolicyRepository pointPolicyRepository;



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

        // 유효하지 않다면 client에게 응답할 메시지 설정
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("1회 적립 가능 포인트는 %d 이상 적립할 수 없습니다.", maxAccumulatePoint)
            ).addConstraintViolation();
        }

        return isValid;
    }
}
