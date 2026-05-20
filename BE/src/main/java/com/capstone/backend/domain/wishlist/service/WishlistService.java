package com.capstone.backend.domain.wishlist.service;

import com.capstone.backend.domain.wishlist.dto.WishlistDto;

import java.util.List;

public interface WishlistService {
    WishlistDto.Response addWishlist(Long userId, WishlistDto.Request request);
    List<WishlistDto.Response> getWishlists(Long userId);
    void removeWishlist(Long userId, Long wishlistId);
}
