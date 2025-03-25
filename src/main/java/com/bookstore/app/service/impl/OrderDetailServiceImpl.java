package com.bookstore.app.service.impl;

import com.bookstore.app.dto.response.OrderDetailResponse;
import com.bookstore.app.entity.Order;
import com.bookstore.app.entity.OrderDetail;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.OrderDetailRepository;
import com.bookstore.app.repository.OrderRepository;
import com.bookstore.app.service.OrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {

    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    ModelMapper modelMapper;

    @Override
    public List<OrderDetailResponse> getAll() {
        return orderDetailRepository.findAll()
                .stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                .toList();
    }

    @Override
    public OrderDetailResponse getOrderDetailById(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Order detail not found"));
        return modelMapper.map(orderDetail, OrderDetailResponse.class);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailsByOrderId(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + orderId + " not found"));
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Order details not found for order ID " + orderId));
        return orderDetails.stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                .toList();
    }
}
