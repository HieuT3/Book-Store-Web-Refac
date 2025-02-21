package com.bookstore.app.service;

import com.bookstore.app.dto.response.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponse> getAll();
}
