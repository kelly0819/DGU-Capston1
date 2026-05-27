package com.capstone.backend.domain.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WishlistListResponse {
    private List<WishlistItemResponse> wishlists;
    private long total;
}
