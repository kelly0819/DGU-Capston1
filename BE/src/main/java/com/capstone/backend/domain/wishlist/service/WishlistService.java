package com.capstone.backend.domain.wishlist.service;

import com.capstone.backend.domain.wishlist.dto.WishlistAddRequest;
import com.capstone.backend.domain.wishlist.dto.WishlistAddResponse;
import com.capstone.backend.domain.wishlist.dto.WishlistListResponse;

import java.util.UUID;

public interface WishlistService {
    WishlistAddResponse addWishlist(UUID userId, WishlistAddRequest request);
    WishlistListResponse getWishlists(UUID userId, int page, int size);
    void removeWishlist(UUID userId, UUID wishlistId);
}
