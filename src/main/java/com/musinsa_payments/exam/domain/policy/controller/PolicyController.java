package com.musinsa_payments.exam.domain.policy.controller;

import com.musinsa_payments.exam.common.dto.ApiResponse;
import com.musinsa_payments.exam.domain.policy.dto.PointPolicyDto;
import com.musinsa_payments.exam.domain.policy.dto.PolicyUpsertRequestDto;
import com.musinsa_payments.exam.domain.policy.dto.UserPointPolicyDto;
import com.musinsa_payments.exam.domain.policy.dto.UserPointPolicyUpsertRequestDto;
import com.musinsa_payments.exam.domain.policy.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class PolicyController {
    private final PolicyService pointPolicyService;

    @PostMapping("/point-policies")
    public ResponseEntity<ApiResponse<PointPolicyDto>> upsertPointPolicy(@RequestBody PolicyUpsertRequestDto request) {
        return ApiResponse.ok(pointPolicyService.upsertPointPolicy(request));
    }

    @PostMapping("/user-point-policies")
    public ResponseEntity<ApiResponse<UserPointPolicyDto>> upsertPointPolicy(@RequestBody UserPointPolicyUpsertRequestDto request) {
        return ApiResponse.ok(pointPolicyService.upsertUserPointPolicy(request));
    }
}
