package com.itgenius.ordermgtsystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(

        @NotBlank(message = "고객명은 필수입니다.")
        String customerName,

        @NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다.")
        @Valid
        List<OrderItemRequest> items
) {

    public record OrderItemRequest(

            @NotNull(message = "상품 ID는 필수입니다.")
            Long productId,

            @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
            int quantity
    ) {}
}
