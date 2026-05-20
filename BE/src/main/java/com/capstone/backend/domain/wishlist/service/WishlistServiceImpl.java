package com.capstone.backend.domain.wishlist.service;

import com.capstone.backend.domain.wishlist.dto.WishlistDto;
import com.capstone.backend.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public WishlistDto.Response addWishlist(Long userId, WishlistDto.Request request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<WishlistDto.Response> getWishlists(Long userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void removeWishlist(Long userId, Long wishlistId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
