package com.capstone.backend.domain.wishlist.controller;

import com.capstone.backend.common.response.ApiResponse;
import com.capstone.backend.domain.wishlist.dto.WishlistAddRequest;
import com.capstone.backend.domain.wishlist.dto.WishlistAddResponse;
import com.capstone.backend.domain.wishlist.dto.WishlistListResponse;
import com.capstone.backend.domain.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
@Tag(name = "6. Wishlist", description = "찜 API")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "찜 추가")
    @PostMapping
    public ResponseEntity<ApiResponse<WishlistAddResponse>> addWishlist(
            @AuthenticationPrincipal UUID userId,
            @RequestBody WishlistAddRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(wishlistService.addWishlist(userId, request)));
    }

    @Operation(summary = "찜 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<WishlistListResponse>> getWishlists(
            @AuthenticationPrincipal UUID userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        WishlistListResponse result = wishlistService.getWishlists(userId, page, size);
        ApiResponse.Meta meta = new ApiResponse.Meta(page, size, result.getTotal());
        return ResponseEntity.ok(ApiResponse.success(result, meta));
    }

    @Operation(summary = "찜 삭제")
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<ApiResponse<Void>> removeWishlist(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID wishlistId) {
        wishlistService.removeWishlist(userId, wishlistId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
