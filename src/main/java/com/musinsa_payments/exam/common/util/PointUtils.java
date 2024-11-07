package com.musinsa_payments.exam.common.util;

public class PointUtils {

    private PointUtils() {
    }

    /**
     * 주어진 값의 부호를 반전시킵니다.
     *
     * @param value 부호를 반전할 값
     * @return 음수는 양수로, 양수는 음수로 반전된 값
     */
    public static long reverseSign(long value) {
        return -value;
    }
}
