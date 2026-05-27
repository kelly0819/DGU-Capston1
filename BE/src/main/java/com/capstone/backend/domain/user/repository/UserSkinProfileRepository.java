package com.capstone.backend.domain.user.repository;

import com.capstone.backend.domain.user.entity.UserSkinProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSkinProfileRepository extends JpaRepository<UserSkinProfile, UUID> {
    Optional<UserSkinProfile> findByUserId(UUID userId);
}
