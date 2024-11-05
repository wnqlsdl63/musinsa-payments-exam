package com.musinsa_payments.exam.domain.point.annotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxAccumulatePointValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxAccumulatePoint {
    String message() default "적립 가능한 최대 포인트를 초과하였습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}