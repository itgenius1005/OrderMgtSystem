package com.itgenius.ordermgtsystem.domain;

import com.itgenius.ordermgtsystem.common.BaseTimeEntity;
import com.itgenius.ordermgtsystem.domain.enums.OrderStatus;
import com.itgenius.ordermgtsystem.exception.BusinessException;
import com.itgenius.ordermgtsystem.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String orderNumber;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order create(String customerName) {
        Order order = new Order();
        order.orderNumber = UUID.randomUUID().toString();
        order.customerName = customerName;
        order.status = OrderStatus.PENDING;
        order.totalAmount = BigDecimal.ZERO;
        return order;
    }

    public void addItem(Product product, int quantity) {
        OrderItem item = OrderItem.create(this, product, quantity);
        this.orderItems.add(item);
        recalculateTotalAmount();
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED) {
            throw new BusinessException(ErrorCode.CANNOT_CANCEL_ORDER);
        }
        this.status = OrderStatus.CANCELLED;
    }

    private void recalculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
