package com.itgenius.ordermgtsystem.service.impl;

import com.itgenius.ordermgtsystem.domain.Order;
import com.itgenius.ordermgtsystem.domain.Product;
import com.itgenius.ordermgtsystem.domain.Stock;
import com.itgenius.ordermgtsystem.dto.request.CreateOrderRequest;
import com.itgenius.ordermgtsystem.dto.response.OrderResponse;
import com.itgenius.ordermgtsystem.exception.BusinessException;
import com.itgenius.ordermgtsystem.exception.ErrorCode;
import com.itgenius.ordermgtsystem.exception.OutOfStockException;
import com.itgenius.ordermgtsystem.repository.OrderRepository;
import com.itgenius.ordermgtsystem.repository.ProductRepository;
import com.itgenius.ordermgtsystem.repository.StockRepository;
import com.itgenius.ordermgtsystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = Order.create(request.customerName());

        for (CreateOrderRequest.OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

            Stock stock = stockRepository.findByProductIdWithLock(itemRequest.productId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));

            validateStockOrThrow(stock, itemRequest.productId(), itemRequest.quantity());
            stock.decrease(itemRequest.quantity());
            order.addItem(product, itemRequest.quantity());
        }

        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        return toResponse(findOrderById(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public OrderResponse confirmOrder(Long orderId) {
        Order order = orderRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        order.confirm();
        return toResponse(order);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        // 취소 시 재고 복구
        order.getOrderItems().forEach(item ->
                stockRepository.findByProductIdWithLock(item.getProduct().getId())
                        .ifPresent(stock -> stock.increase(item.getQuantity()))
        );

        order.cancel();
        return toResponse(order);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

        private void validateStockOrThrow(Stock stock, Long productId, int requestedQuantity) {
                if (stock.getQuantity() < requestedQuantity) {
                        throw new OutOfStockException(productId, stock.getQuantity(), requestedQuantity);
                }
        }

    private OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> new OrderResponse.OrderItemResponse(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getStatus(),
                order.getTotalAmount(),
                itemResponses,
                order.getCreatedAt()
        );
    }
}
