package com.capstone.backend.domain.recommendation.service;

import com.capstone.backend.domain.recommendation.dto.RecommendationDto;
import com.capstone.backend.domain.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public List<RecommendationDto.Response> getRecommendations(UUID userId) {
        return Collections.emptyList();
    }
}
