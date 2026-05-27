package com.capstone.backend.domain.wishlist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class WishlistAddRequest {
    private UUID productId;
}
