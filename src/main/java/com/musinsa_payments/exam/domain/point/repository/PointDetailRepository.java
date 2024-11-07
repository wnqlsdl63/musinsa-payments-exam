package com.musinsa_payments.exam.domain.point.repository;

import com.musinsa_payments.exam.domain.point.dto.AvailablePointDetailDto;
import com.musinsa_payments.exam.domain.point.entity.PointDetail;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {
    // 특정 적립 포인트가 사용된 적이 있는지 확인
    boolean existsByDetailPointIdAndStatus(Long detailPointId, PointStatus status);

    @Query("""
                SELECT new com.musinsa_payments.exam.domain.point.dto.AvailablePointDetailDto(
                    pd.detailPointId, SUM(pd.amount), MIN(pd.status), MIN(pd.expireDate)
                )
                FROM PointDetail pd
                WHERE pd.userId = :userId
                GROUP BY pd.detailPointId
                HAVING SUM(pd.amount) > 0
                ORDER BY 
                    CASE MIN(pd.status) WHEN 'ADMIN_ACCUMULATED' THEN 0 ELSE 1 END,
                    MIN(pd.expireDate) ASC
            """)
    List<AvailablePointDetailDto> getAvailablePointsByUserId(@Param("userId") Long userId);
}
