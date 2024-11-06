package com.musinsa_payments.exam.domain.point.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpireDateValidator implements ConstraintValidator<ValidExpireDate, String> {

    @Override
    public boolean isValid(String expireDate, ConstraintValidatorContext context) {
        if (expireDate == null || expireDate.isBlank()) {
            return true;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(expireDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate minDate = LocalDate.now().plusDays(1);
            LocalDate maxDate = LocalDate.now().plusYears(5).minusDays(1);

            return !parsedDate.isBefore(minDate) && !parsedDate.isAfter(maxDate);
        } catch (DateTimeParseException e) { // 날짜 형식이 잘못된 경우
            return false;
        }
    }
}
