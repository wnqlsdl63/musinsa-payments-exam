package com.musinsa_payments.exam.domain.point.service;

import com.musinsa_payments.exam.domain.point.dto.PointAccumulateRequestDto;
import com.musinsa_payments.exam.domain.point.dto.PointDto;
import com.musinsa_payments.exam.domain.point.entity.Point;
import com.musinsa_payments.exam.domain.point.entity.PointDetail;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import com.musinsa_payments.exam.domain.point.repository.PointDetailRepository;
import com.musinsa_payments.exam.domain.point.repository.PointRepository;
import com.musinsa_payments.exam.domain.point.service.validator.PointValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {
    private final PointRepository pointsRepository;
    private final PointDetailRepository pointDetailRepository;
    private final PointValidator pointValidator;

    @Transactional
    public PointDto accumulatePoints(PointAccumulateRequestDto request, Boolean isManual) {

        // 유효성 검사
        pointValidator.validateAccumulatePoints(request.userId(), request.amount());

        // 관리자 여부에 따라 PointStatus 설정
        PointStatus pointStatus = isManual ? PointStatus.ADMIN_ACCUMULATED : PointStatus.ACCUMULATED;

        // 포인트 적립 정보 저장
        Point point = Point.createPoint(
                request.userId(),
                request.amount(),
                request.orderId(),
                pointStatus,
                request.getExpireDateOrDefault()
        );
        pointsRepository.save(point);

        // PointDetail 생성 및 저장
        PointDetail pointDetail = PointDetail.createPointDetail(point, point.getId(), request.amount(), pointStatus);
        pointDetailRepository.save(pointDetail);

        return point.toDto();
    }

    @Transactional
    public PointDto cancelAccumulatePoint(Long pointId) {
        Point originPoint = pointsRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 포인트가 존재하지 않습니다."));

        pointValidator.validateCancelAccumulatePoint(pointId);

        // 취소 포인트 생성 (amount를 마이너스로 설정)
        Point cancelPoint = Point.createPoint(
                originPoint.getUserId(),
                -originPoint.getPoint(),  // 기존 적립 금액을 마이너스로 설정
                originPoint.getOrderId(),
                PointStatus.ACCUMULATION_CANCELLED,
                null
        );
        pointsRepository.save(cancelPoint);

        // 취소에 대한 PointDetail 생성 및 저장
        PointDetail pointDetail = PointDetail.createPointDetail(cancelPoint, originPoint.getId(), -originPoint.getPoint(), PointStatus.ACCUMULATION_CANCELLED);
        pointDetailRepository.save(pointDetail);

        return cancelPoint.toDto();
    }
}
