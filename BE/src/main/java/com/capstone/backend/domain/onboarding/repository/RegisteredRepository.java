package com.capstone.backend.domain.onboarding.repository;

import com.capstone.backend.domain.onboarding.entity.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegisteredRepository extends JpaRepository<Registered, UUID> {
    List<Registered> findByUserId(UUID userId);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
    long countByUserId(UUID userId);

    @Query("SELECT r FROM Registered r WHERE r.id = :id AND r.user.id = :userId")
    Optional<Registered> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
