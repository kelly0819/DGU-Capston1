package com.capstone.backend.domain.recommendation.repository;

import com.capstone.backend.domain.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecommendationRepository extends JpaRepository<Recommendation, String> {
    List<Recommendation> findByUserIdOrderByProgressDesc(UUID userId);
}
