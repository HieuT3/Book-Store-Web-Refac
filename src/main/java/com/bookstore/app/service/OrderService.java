package com.bookstore.app.service;

import com.bookstore.app.constant.OrderStatusType;
import com.bookstore.app.dto.request.OrderRequest;
import com.bookstore.app.dto.response.OrderResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAll();
    OrderResponse getOrderById(Long orderId);
    OrderResponse createOrder(String cartId, OrderRequest orderRequest, HttpServletResponse response);
    OrderResponse updateInfoOrder(Long orderId, OrderRequest orderRequest);
    OrderResponse updateStatusOrder(Long orderId, OrderStatusType orderStatusType);
    void deleteOrderById(Long orderId);
}
