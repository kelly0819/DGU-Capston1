package com.capstone.backend.domain.onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RegisteredProductAddResponse {
    private UUID registeredId;
    private UUID productId;
    private String name;
    private String brand;
    private String imageUrl;
    private boolean isNewProduct;
}
