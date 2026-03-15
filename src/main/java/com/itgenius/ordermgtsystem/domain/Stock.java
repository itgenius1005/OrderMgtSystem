package com.itgenius.ordermgtsystem.domain;

import com.itgenius.ordermgtsystem.common.BaseTimeEntity;
import com.itgenius.ordermgtsystem.exception.BusinessException;
import com.itgenius.ordermgtsystem.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public static Stock create(Product product, int initialQuantity) {
        Stock stock = new Stock();
        stock.product = product;
        stock.quantity = initialQuantity;
        return stock;
    }

    public void decrease(int amount) {
        if (this.quantity < amount) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.quantity -= amount;
    }

    public void increase(int amount) {
        this.quantity += amount;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
