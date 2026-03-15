package com.itgenius.ordermgtsystem.service;

import com.itgenius.ordermgtsystem.dto.request.CreateOrderRequest;
import com.itgenius.ordermgtsystem.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrder(Long orderId);

    OrderResponse getOrderByOrderNumber(String orderNumber);

    List<OrderResponse> getAllOrders();

    OrderResponse confirmOrder(Long orderId);

    OrderResponse cancelOrder(Long orderId);
}
