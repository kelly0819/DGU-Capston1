package com.capstone.backend.domain.user.entity;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_skin_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSkinProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "personal_color", length = 20)
    private String personalColor;

    @Column(name = "skin_type", length = 20)
    private String skinType;

    @Type(StringArrayType.class)
    @Column(name = "skin_concerns", columnDefinition = "text[]", nullable = false)
    @Builder.Default
    private String[] skinConcerns = new String[0];

    @Type(StringArrayType.class)
    @Column(name = "notes", columnDefinition = "text[]")
    private String[] notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(String personalColor, String skinType, String[] skinConcerns, String[] notes) {
        if (personalColor != null) this.personalColor = personalColor;
        if (skinType != null) this.skinType = skinType;
        if (skinConcerns != null) this.skinConcerns = skinConcerns;
        if (notes != null) this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
}
