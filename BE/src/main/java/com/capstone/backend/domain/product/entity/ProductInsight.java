package com.capstone.backend.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_insights")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

<<<<<<< HEAD
    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;
=======
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "original_price")
    private Integer originalPrice;
>>>>>>> 297f7cb (chore: stash:)

    @Column(name = "lowest_price")
    private Integer lowestPrice;

<<<<<<< HEAD
    @Column(name = "current_price")
    private Integer currentPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
=======
    @Column(name = "stores", columnDefinition = "jsonb")
    private String stores;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
>>>>>>> 297f7cb (chore: stash:)
}
