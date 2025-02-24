package com.bookstore.app.service.impl;

import com.bookstore.app.dto.response.OrderDetailResponse;
import com.bookstore.app.repository.OrderDetailRepository;
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
    OrderDetailRepository orderDetailRepository;
    ModelMapper modelMapper;

    @Override
    public List<OrderDetailResponse> getAll() {
        return orderDetailRepository.findAll()
                .stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailResponse.class))
                .toList();
    }
}
