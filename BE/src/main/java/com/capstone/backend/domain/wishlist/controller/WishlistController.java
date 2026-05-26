package com.capstone.backend.domain.wishlist.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.common.util.SecurityUtil;
import com.capstone.backend.domain.wishlist.dto.WishlistDto;
import com.capstone.backend.domain.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<ApiResponse<WishlistDto.Response>> addWishlist(@RequestBody WishlistDto.Request request) {
        UUID userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(wishlistService.addWishlist(userId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishlistDto.Response>>> getWishlists() {
        UUID userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(wishlistService.getWishlists(userId)));
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<ApiResponse<Void>> removeWishlist(@PathVariable UUID wishlistId) {
        UUID userId = SecurityUtil.getCurrentUserId();
        wishlistService.removeWishlist(userId, wishlistId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
