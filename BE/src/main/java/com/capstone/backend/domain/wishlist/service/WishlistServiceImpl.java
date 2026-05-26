package com.capstone.backend.domain.wishlist.service;

import com.capstone.backend.common.exception.BusinessException;
import com.capstone.backend.common.exception.ErrorCode;
import com.capstone.backend.domain.wishlist.dto.WishlistDto;
import com.capstone.backend.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public WishlistDto.Response addWishlist(UUID userId, WishlistDto.Request request) {
        throw new BusinessException(ErrorCode.NOT_FOUND);
    }

    @Override
    public List<WishlistDto.Response> getWishlists(UUID userId) {
        return Collections.emptyList();
    }

    @Override
    public void removeWishlist(UUID userId, UUID wishlistId) {
    }
}
