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
    public PointDto accumulatePoints(PointAccumulateRequestDto request) {

        // 유효성 검사
        pointValidator.validateAccumulatePoints(request.userId(), request.amount());

        // 포인트 적립 정보 저장
        Point point = Point.createPoint(
                request.userId(),
                request.amount(),
                request.orderId(),
                PointStatus.ACCUMULATED,
                request.getExpireDateOrDefault()
        );
        pointsRepository.save(point);

        // PointDetail 생성 및 저장
        PointDetail pointDetail = PointDetail.createPointDetail(point, request.amount(), PointStatus.ACCUMULATED);
        pointDetailRepository.save(pointDetail);

        return point.toDto();
    }
}
