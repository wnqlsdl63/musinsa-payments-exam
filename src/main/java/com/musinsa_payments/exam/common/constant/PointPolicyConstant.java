package com.musinsa_payments.exam.common.constant;

public class PointPolicyConstant {
    /**
     * 최대 적립 가능 포인트의 기본값 (1회 적립 시 10만 포인트까지 가능).
     * DB에 정책 값이 없을 경우 사용할 기본 최대 적립 포인트 값입니다.
     */
    public static final int DEFAULT_MAX_ACCUMULATE_POINT = 100000;

    /**
     * 사용자가 보유할 수 있는 최대 포인트의 기본값 (20만 포인트).
     * 특정 사용자의 포인트 정책이 없을 경우 사용할 기본 최대 보유 포인트 값입니다.
     */
    public static final int DEFAULT_USER_MAX_POINT = 200000;

    /**
     * 최대 적립 가능 포인트 정책을 식별하기 위한 설정 키.
     * PointPolicy 테이블에서 최대 적립 포인트 정책을 조회할 때 사용됩니다.
     */
    public static final String MAX_ACCUMULATE_POINT_KEY = "MAX_ACCUMULATE_POINT";
}
