package com.musinsa_payments.exam.domain.point.controller;

import com.musinsa_payments.exam.common.dto.ApiResponse;
import com.musinsa_payments.exam.domain.point.dto.PointAccumulateRequestDto;
import com.musinsa_payments.exam.domain.point.dto.PointDto;
import com.musinsa_payments.exam.domain.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
public class PointController {
    private final PointService pointService;

    @PostMapping("/points")
    public ResponseEntity<ApiResponse<PointDto>> accumulatePoint(@Valid @RequestBody PointAccumulateRequestDto request) {
        log.info("accumulatePoint request: {}", request);
        return ApiResponse.ok(pointService.accumulatePoints(request));
    }
}
