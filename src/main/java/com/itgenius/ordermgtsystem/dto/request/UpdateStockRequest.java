package com.itgenius.ordermgtsystem.dto.request;

import jakarta.validation.constraints.Min;

public record UpdateStockRequest(

        @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
        int quantity
) {}
