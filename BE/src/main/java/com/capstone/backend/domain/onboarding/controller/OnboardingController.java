package com.capstone.backend.domain.onboarding.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.user.dto.request.SkinProfileRequest;
import com.capstone.backend.domain.user.dto.response.OnboardingCompleteResponse;
import com.capstone.backend.domain.user.dto.response.SkinProfileResponse;
import com.capstone.backend.domain.user.service.UserProfileService;
import com.capstone.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
@Tag(name = "3. Onboarding", description = "온보딩 API")
public class OnboardingController {

    private final UserService userService;
    private final UserProfileService userProfileService;

    @Operation(summary = "온보딩 완료 처리")
    @PatchMapping
    public ResponseEntity<ApiResponse<OnboardingCompleteResponse>> completeOnboarding(
            @AuthenticationPrincipal UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.completeOnboarding(userId)));
    }

    @Operation(summary = "피부 정보 저장 (온보딩)")
    @PostMapping("/skin-profile")
    public ResponseEntity<ApiResponse<SkinProfileResponse>> saveSkinProfile(
            @AuthenticationPrincipal UUID userId,
            @RequestBody SkinProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userProfileService.saveSkinProfile(userId, request)));
    }
}