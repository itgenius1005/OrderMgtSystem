package com.itgenius.ordermgtsystem.exception;

public class OutOfStockException extends BusinessException {

    public OutOfStockException(Long productId, int availableQuantity, int requestedQuantity) {
        super(
                ErrorCode.INSUFFICIENT_STOCK,
                String.format(
                        "재고가 부족합니다. productId=%d, available=%d, requested=%d",
                        productId,
                        availableQuantity,
                        requestedQuantity
                )
        );
    }
}