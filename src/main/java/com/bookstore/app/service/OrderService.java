package com.bookstore.app.service;

import com.bookstore.app.constant.OrderStatusType;
import com.bookstore.app.dto.request.OrderRequest;
import com.bookstore.app.dto.response.OrderResponse;
import com.bookstore.app.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAll();
    OrderResponse getOrderById(Long orderId);
    List<OrderResponse> getOrderByUser(User user);
    OrderResponse createOrder(String cartId, OrderRequest orderRequest, HttpServletResponse response);
    OrderResponse updateInfoOrder(Long orderId, OrderRequest orderRequest);
    OrderResponse updateStatusOrder(Long orderId, OrderStatusType orderStatusType);
    void deleteOrderById(Long orderId);
}
