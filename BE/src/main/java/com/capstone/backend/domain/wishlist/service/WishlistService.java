package com.capstone.backend.domain.wishlist.service;

import com.capstone.backend.domain.wishlist.dto.WishlistDto;

import java.util.List;
import java.util.UUID;

public interface WishlistService {
    WishlistDto.Response addWishlist(UUID userId, WishlistDto.Request request);
    List<WishlistDto.Response> getWishlists(UUID userId);
    void removeWishlist(UUID userId, UUID wishlistId);
}
