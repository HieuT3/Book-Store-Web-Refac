package com.bookstore.app.service;

import com.bookstore.app.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAll();
}
