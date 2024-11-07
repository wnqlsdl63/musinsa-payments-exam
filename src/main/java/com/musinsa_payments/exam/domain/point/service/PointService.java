package com.musinsa_payments.exam.domain.point.service;

import com.musinsa_payments.exam.domain.point.dto.AvailablePointDetailDto;
import com.musinsa_payments.exam.domain.point.dto.PointAccumulateRequestDto;
import com.musinsa_payments.exam.domain.point.dto.PointDto;
import com.musinsa_payments.exam.domain.point.dto.PointUseRequestDto;
import com.musinsa_payments.exam.domain.point.entity.Point;
import com.musinsa_payments.exam.domain.point.entity.PointDetail;
import com.musinsa_payments.exam.domain.point.enums.PointStatus;
import com.musinsa_payments.exam.domain.point.repository.PointDetailRepository;
import com.musinsa_payments.exam.domain.point.repository.PointRepository;
import com.musinsa_payments.exam.domain.point.service.validator.PointValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        Point accumulatePoint = Point.createAccumulatePoint(request, pointStatus);
        pointsRepository.save(accumulatePoint);

        // PointDetail 생성 및 저장
        PointDetail pointDetail = PointDetail.createAccumulatePointDetail(accumulatePoint, request.amount(), pointStatus);
        pointDetailRepository.save(pointDetail);

        return accumulatePoint.toDto();
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

    @Transactional
    public PointDto usePoint(PointUseRequestDto request) {
        log.debug("[usePoint] request: {}", request);
        // 포인트 사용 유효성 검사
        pointValidator.validateSufficientPoints(request.userId(), request.amount());

        // 사용 포인트 생성
        final Point usePoint = Point.createUsePoint(request);
        pointsRepository.save(usePoint);

        // 남은 사용 금액 초기화
        long remainingAmount = request.amount();
        final List<PointDetail> usePointDetails = new ArrayList<>();

        // 사용 가능한 포인트 목록 조회
        final List<AvailablePointDetailDto> availablePoints = pointDetailRepository.getAvailablePointsByUserId(request.userId());
        log.debug("[usePoint] availablePoints: {}", availablePoints);

        // 사용 가능한 포인트를 순회하며 차감
        for (AvailablePointDetailDto availablePoint : availablePoints) {
            if (remainingAmount <= 0) break; // 남은 금액이 0이 되면 종료
            log.debug("[usePoint] remainingAmount: {}", remainingAmount);

            // 사용할 수 있는 금액과 남은 금액 중 작은 값을 차감
            final long usableAmount = Math.min(availablePoint.sumAmount(), remainingAmount);
            remainingAmount -= usableAmount;

            final PointDetail usePointDetail = PointDetail.createUsePointDetail(
                    usePoint,
                    availablePoint.detailPointId(),
                    usableAmount
            );

            usePointDetails.add(usePointDetail);
        }

        pointDetailRepository.saveAll(usePointDetails);

        return usePoint.toDto();
    }


}
