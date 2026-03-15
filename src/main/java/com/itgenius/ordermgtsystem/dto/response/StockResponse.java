package com.itgenius.ordermgtsystem.dto.response;

import java.time.LocalDateTime;

public record StockResponse(
        Long id,
        Long productId,
        String productName,
        int quantity,
        LocalDateTime updatedAt
) {}
