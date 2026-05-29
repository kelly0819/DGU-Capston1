package com.capstone.backend.domain.product.repository;

import com.capstone.backend.domain.product.entity.ProductInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductInsightRepository extends JpaRepository<ProductInsight, UUID> {
    Optional<ProductInsight> findByProduct_Id(UUID productId);
}
