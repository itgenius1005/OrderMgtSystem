package com.itgenius.ordermgtsystem.service.impl;

import com.itgenius.ordermgtsystem.domain.Order;
import com.itgenius.ordermgtsystem.domain.Product;
import com.itgenius.ordermgtsystem.domain.Stock;
import com.itgenius.ordermgtsystem.dto.request.CreateOrderRequest;
import com.itgenius.ordermgtsystem.dto.response.OrderResponse;
import com.itgenius.ordermgtsystem.exception.OutOfStockException;
import com.itgenius.ordermgtsystem.repository.OrderRepository;
import com.itgenius.ordermgtsystem.repository.ProductRepository;
import com.itgenius.ordermgtsystem.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("BVA: 재고가 1개(경계값)이면 주문에 성공한다")
    void createOrder_success_whenStockIsOne() {
        Long productId = 1L;
        Product product = Product.create("Keyboard", "Mechanical keyboard", new BigDecimal("1000"));
        Stock stock = Stock.create(product, 1);
        CreateOrderRequest request = new CreateOrderRequest(
                "홍길동",
                List.of(new CreateOrderRequest.OrderItemRequest(productId, 1))
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(stockRepository.findByProductIdWithLock(productId)).thenReturn(Optional.of(stock));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        assertEquals(0, stock.getQuantity());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("BVA: 재고가 0개(경계값 미만)이면 OutOfStockException을 던진다")
    void createOrder_fail_whenStockIsZero() {
        Long productId = 1L;
        Product product = Product.create("Keyboard", "Mechanical keyboard", new BigDecimal("1000"));
        Stock stock = Stock.create(product, 0);
        CreateOrderRequest request = new CreateOrderRequest(
                "홍길동",
                List.of(new CreateOrderRequest.OrderItemRequest(productId, 1))
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(stockRepository.findByProductIdWithLock(productId)).thenReturn(Optional.of(stock));

        assertThrows(OutOfStockException.class, () -> orderService.createOrder(request));

        assertEquals(0, stock.getQuantity());
        verify(orderRepository, never()).save(any(Order.class));
    }
}