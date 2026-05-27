package com.capstone.backend.domain.wishlist.dto;

import com.capstone.backend.domain.wishlist.entity.Wishlist;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class WishlistAddResponse {
    private UUID wishlistId;
    private UUID productId;
    private LocalDateTime createdAt;

    public static WishlistAddResponse from(Wishlist wishlist) {
        return WishlistAddResponse.builder()
                .wishlistId(wishlist.getId())
                .productId(wishlist.getProduct().getId())
                .createdAt(wishlist.getCreatedAt())
                .build();
    }
}
