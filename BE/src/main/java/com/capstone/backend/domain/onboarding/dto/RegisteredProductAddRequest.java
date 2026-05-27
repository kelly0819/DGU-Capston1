package com.capstone.backend.domain.onboarding.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisteredProductAddRequest {
    private String externalId;
    private String name;
    private String brand;
    private String category;
    private String imageUrl;
    private Integer lowestPrice;
}
