package com.musinsa_payments.exam.domain.point.service;

import com.musinsa_payments.exam.domain.point.dto.*;
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


    /**
     * 사용자의 포인트를 적립합니다.
     * - 포인트 적립 유효성 검사를 통해 사용자가 최대 적립 가능한 포인트를 초과하지 않도록 검증합니다.
     * - 관리자 수기 적립 여부에 따라 포인트 상태값을 설정하고 적립된 포인트 정보를 저장합니다.
     *
     * @param request 포인트 적립 요청 정보
     * @param isManual 수기 적립 여부
     * @return 적립된 포인트 정보를 반환하는 DTO
     */
    @Transactional
    public PointDto accumulatePoint(PointAccumulateRequestDto request, Boolean isManual) {

        // 포인트 적립 유효성 검사
        pointValidator.validateAccumulatePoints(request.userId(), request.amount());

        // 관리자 여부에 따라 PointStatus 설정
        final PointStatus pointStatus = isManual ? PointStatus.ADMIN_ACCUMULATED : PointStatus.ACCUMULATED;

        // 포인트 적립 정보 생성 및 저장
        final Point accumulatePoint = Point.createAccumulatePoint(request, pointStatus);
        pointsRepository.save(accumulatePoint);

        // PointDetail 생성 및 저장
        final PointDetail pointDetail = PointDetail.createAccumulatePointDetail(accumulatePoint, request.amount(), pointStatus);
        pointDetailRepository.save(pointDetail);

        return accumulatePoint.toDto();
    }

    /**
     * 사용자의 적립된 포인트를 취소합니다.
     * - 적립된 포인트가 사용되었거나 취소 불가능한 상태값인지 확인하여 검증합니다.
     *
     * @param pointId 취소할 포인트 ID
     * @return 취소된 포인트 정보를 반환하는 DTO
     */
    @Transactional
    public PointDto cancelAccumulatePoint(Long pointId) {
        Point originPoint = pointsRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 포인트가 존재하지 않습니다."));

        // 포인트 적립 취소 유효성 검사
        pointValidator.validateCancelAccumulatePoint(originPoint);

        // 취소 포인트 생성
        final Point cancelPoint = Point.createAccumlateCanclePoint(originPoint, PointStatus.ACCUMULATION_CANCELLED);
        pointsRepository.save(cancelPoint);

        // 취소에 대한 PointDetail 생성
        final PointDetail cancelPointDetail = PointDetail.createAccumulateCacnelPointDetail(
                cancelPoint,
                originPoint,
                PointStatus.ACCUMULATION_CANCELLED
        );
        pointDetailRepository.save(cancelPointDetail);

        return cancelPoint.toDto();
    }

    /**
     * 사용자의 포인트를 사용합니다,
     * - 사용자의 포인트 잔액이 충분한지 검증합니다.
     * - 사용 요청 금액이 사용 가능한 포인트로 차감될 수 있도록 관리하며,
     *   각 사용 기록을 PointDetail로 생성하고 저장합니다.
     *
     * @param request 포인트 사용 요청 정보
     * @return 사용된 포인트 정보를 반환하는 DTO
     */
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
        final List<AvailablePointDetailDto> availablePointDetails = pointDetailRepository.getAvailablePointsByUserId(request.userId());
        log.debug("[usePoint] availablePoints: {}", availablePointDetails);

        // 사용 가능한 PointDetails를 순회하며 차감
        for (AvailablePointDetailDto availablePoint : availablePointDetails) {
            if (remainingAmount <= 0) break; // 남은 금액이 0이 되면 종료
            log.debug("[usePoint] remainingAmount: {}", remainingAmount);

            // 현재 PointDetail의 포인트에서 차감할 금액 계산
            long usableAmount = calculateUsableAmount(availablePoint.sumAmount(), remainingAmount);
            remainingAmount = updateRemainingAmount(remainingAmount, usableAmount);

            usePointDetails.add(
                    PointDetail.createUsePointDetail(
                    usePoint,
                    availablePoint,
                    usableAmount
            ));
        }

        pointDetailRepository.saveAll(usePointDetails);

        return usePoint.toDto();
    }

//    public PointDto cancelUsagePoint(Long pointId, PointUsageCancelRequestDto request) {
//        // pointId로 PointDetail.originPointId 조회(List<PointDetail>)
//
//
//        return null;
//    }

    /**
     * 사용 가능한 포인트와 남은 요청 금액 중에서 실제로 사용할 수 있는 금액을 계산하여 반환합니다.
     *
     * @param availableAmount 현재 사용할 수 있는 포인트 금액
     * @param remainingAmount 요청된 사용 포인트 중 남은 포인트
     * @return 현재 차감할 수 있는 포인트 금액
     */
    private long calculateUsableAmount(long availableAmount, long remainingAmount) {
        return Math.min(availableAmount, remainingAmount);
    }

    /**
     * 남은 요청 금액에서 현재 사용된 금액을 차감하여, 갱신된 남은 요청 금액을 반환합니다.
     *
     * @param remainingAmount 현재 남아 있는 요청 금액
     * @param usableAmount 이번에 사용된 포인트 금액
     * @return 차감 후 남아 있는 사용 포인트 금액
     */
    private long updateRemainingAmount(long remainingAmount, long usableAmount) {
        return remainingAmount - usableAmount;
    }


}
