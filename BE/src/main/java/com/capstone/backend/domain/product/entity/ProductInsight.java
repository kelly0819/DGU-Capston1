package com.capstone.backend.domain.product.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "original_price")
    private Integer originalPrice;

    @Column(name = "lowest_price")
    private Integer lowestPrice;

    @Column(name = "savings")
    private Integer savings;

    @Type(JsonType.class)
    @Column(name = "stores", columnDefinition = "jsonb")
    private String stores;

    @Column(name = "review_summary")
    private String reviewSummary;

    @Column(name = "average_score")
    private BigDecimal averageScore;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Type(JsonType.class)
    @Column(name = "skin_type_satisfaction", columnDefinition = "jsonb")
    private String skinTypeSatisfaction;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    public void updatePriceData(Integer lowestPrice, String stores) {
        if (lowestPrice != null) this.lowestPrice = lowestPrice;
        if (stores != null) this.stores = stores;
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
