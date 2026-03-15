package com.itgenius.ordermgtsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank(message = "상품명은 필수입니다.")
        String name,

        String description,

        @NotNull(message = "가격은 필수입니다.")
        @DecimalMin(value = "0.01", message = "가격은 0.01 이상이어야 합니다.")
        BigDecimal price,

        @Min(value = 0, message = "초기 재고는 0 이상이어야 합니다.")
        int initialStock
) {}
