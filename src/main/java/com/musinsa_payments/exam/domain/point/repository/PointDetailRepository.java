package com.musinsa_payments.exam.domain.point.repository;

import com.musinsa_payments.exam.domain.point.entity.PointDetail;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {
    // 특정 적립 포인트가 사용된 적이 있는지 확인
    boolean existsByDetailPointIdAndStatus(Long detailPointId, PointStatus status);
}
