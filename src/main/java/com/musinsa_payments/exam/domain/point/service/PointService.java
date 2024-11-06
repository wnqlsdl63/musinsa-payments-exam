package com.musinsa_payments.exam.domain.point.service;

import com.musinsa_payments.exam.common.util.PointUtils;
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
    public PointDto accumulatePoint(PointAccumulateRequestDto request, Boolean isManual) {

        // 포인트 적립 유효성 검사
        pointValidator.validateAccumulatePoints(request.userId(), request.amount());

        // 관리자 여부에 따라 PointStatus 설정
        PointStatus pointStatus = isManual ? PointStatus.ADMIN_ACCUMULATED : PointStatus.ACCUMULATED;

        // 포인트 적립 정보 생성 및 저장
        Point point = Point.createAccumulatePoint(request, pointStatus);
        pointsRepository.save(point);

        // PointDetail 생성 및 저장
        PointDetail pointDetail = PointDetail.createAccumulatePointDetail(point, request.amount(), pointStatus);
        pointDetailRepository.save(pointDetail);

        return point.toDto();
    }

    @Transactional
    public PointDto cancelAccumulatePoint(Long pointId) {
        Point originPoint = pointsRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 포인트가 존재하지 않습니다."));

        // 포인트 적립 취소 유효성 검사
        pointValidator.validateCancelAccumulatePoint(pointId);

        // 취소 포인트 생성
        Point cancelPoint = Point.createAccumlateCanclePoint(originPoint, PointStatus.ACCUMULATION_CANCELLED);
        pointsRepository.save(cancelPoint);

        // 취소에 대한 PointDetail 생성
        PointDetail cancelPointDetail = PointDetail.createAccumulateCacnelPointDetail(
                cancelPoint,
                originPoint,
                PointStatus.ACCUMULATION_CANCELLED
        );
        pointDetailRepository.save(cancelPointDetail);

        return cancelPoint.toDto();
    }
}
