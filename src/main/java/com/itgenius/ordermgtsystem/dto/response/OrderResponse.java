package com.itgenius.ordermgtsystem.dto.response;

import com.itgenius.ordermgtsystem.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String customerName,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
) {

    public record OrderItemResponse(
            Long id,
            Long productId,
            String productName,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice
    ) {}
}
