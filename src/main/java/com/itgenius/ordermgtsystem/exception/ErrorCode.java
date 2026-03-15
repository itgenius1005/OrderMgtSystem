package com.itgenius.ordermgtsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Order
    ORDER_NOT_FOUND("O001", "주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_ORDER_STATUS("O002", "유효하지 않은 주문 상태입니다.", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_ORDER("O003", "배송 중이거나 완료된 주문은 취소할 수 없습니다.", HttpStatus.BAD_REQUEST),

    // Product
    PRODUCT_NOT_FOUND("P001", "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Stock
    STOCK_NOT_FOUND("S001", "재고 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK("S002", "재고가 부족합니다.", HttpStatus.BAD_REQUEST),

    // Common
    INVALID_INPUT("C001", "잘못된 입력값입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("C002", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
