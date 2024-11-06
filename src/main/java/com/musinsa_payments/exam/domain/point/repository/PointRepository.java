package com.musinsa_payments.exam.domain.point.repository;

import com.musinsa_payments.exam.domain.point.entity.Point;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("""
                SELECT COALESCE(SUM(p.point), 0)
                FROM Point p
                WHERE p.userId = :userId
                AND (p.expireDate IS NULL OR p.expireDate > CURRENT_TIMESTAMP)
            """)
    int getCurrentPointsByUserId(@Param("userId") Long userId);
}
