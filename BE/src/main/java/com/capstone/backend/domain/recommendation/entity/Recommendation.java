package com.capstone.backend.domain.recommendation.entity;

import com.capstone.backend.domain.product.entity.Product;
import com.capstone.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommendation_jobs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Recommendation {

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_product_id", nullable = false)
    private Product product;

    @Column(name = "status")
    private String status;

    @Column(name = "progress")
    private Integer progress;
}
