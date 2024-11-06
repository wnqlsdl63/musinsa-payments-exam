package com.musinsa_payments.exam.domain.point.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ExpireDateValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidExpireDate {
    String message() default "만료일은 현재 날짜로부터 최소 1일 이상, 최대 5년 미만이어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
