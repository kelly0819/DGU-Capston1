package com.capstone.backend.domain.recommendation.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.common.util.SecurityUtil;
import com.capstone.backend.domain.recommendation.dto.RecommendationDto;
import com.capstone.backend.domain.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecommendationDto.Response>>> getRecommendations() {
        UUID userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(recommendationService.getRecommendations(userId)));
    }
}
