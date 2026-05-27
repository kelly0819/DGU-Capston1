package com.capstone.backend.domain.wishlist.repository;

import com.capstone.backend.domain.wishlist.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    List<Wishlist> findByUserId(UUID userId);
    Optional<Wishlist> findByUserIdAndProductId(UUID userId, UUID productId);
    boolean existsByUserIdAndProductId(UUID userId, UUID productId);
    long countByUserId(UUID userId);

    Page<Wishlist> findByUser_IdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    @Query("SELECT w FROM Wishlist w WHERE w.id = :id AND w.user.id = :userId")
    Optional<Wishlist> findByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
