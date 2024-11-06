package com.musinsa_payments.exam.domain.point.repository;

import com.musinsa_payments.exam.domain.point.entity.PointDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {
}
