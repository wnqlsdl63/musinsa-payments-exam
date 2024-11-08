package com.musinsa_payments.exam.domain.point.controller;

import com.musinsa_payments.exam.common.dto.ApiResponse;
import com.musinsa_payments.exam.domain.point.dto.PointAccumulateRequestDto;
import com.musinsa_payments.exam.domain.point.dto.PointDto;
import com.musinsa_payments.exam.domain.point.dto.PointUseRequestDto;
import com.musinsa_payments.exam.domain.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
public class PointController {
    private final PointService pointService;

    @PostMapping("/points/accumulate")
    public ResponseEntity<ApiResponse<PointDto>> accumulatePoint(@Valid @RequestBody PointAccumulateRequestDto request) {
        return ApiResponse.ok(pointService.accumulatePoint(request, false));
    }

    @PostMapping("/points/accumulate/{pointId}/cancel")
    public ResponseEntity<ApiResponse<PointDto>> cancelAccumulatePoint(@PathVariable Long pointId) {
        return ApiResponse.ok(pointService.cancelAccumulatePoint(pointId));
    }

    @PostMapping("/points/usage")
    public ResponseEntity<ApiResponse<PointDto>> usePoint(@Valid @RequestBody PointUseRequestDto request) {
        return ApiResponse.ok(pointService.usePoint(request));
    }

    @PostMapping("/admin/points")
    public ResponseEntity<ApiResponse<PointDto>> accumulatePointsManually(@Valid @RequestBody PointAccumulateRequestDto request) {
        return ApiResponse.ok(pointService.accumulatePoint(request, true));
    }




}
