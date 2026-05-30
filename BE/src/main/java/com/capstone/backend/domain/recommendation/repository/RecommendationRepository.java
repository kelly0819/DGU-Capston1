package com.capstone.backend.domain.recommendation.repository;

import com.capstone.backend.domain.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {

    @Query("SELECT r FROM Recommendation r WHERE r.id = :id AND r.user.id = :userId")
    Optional<Recommendation> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
