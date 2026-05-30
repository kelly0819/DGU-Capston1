package com.capstone.backend.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecognizeRequest {

    @NotBlank(message = "type은 필수입니다.")
    private String type;   // TEXT | IMAGE | NFC

    private String keyword;  // type=TEXT 필수
    private String nfcData;  // type=NFC 예정
}
