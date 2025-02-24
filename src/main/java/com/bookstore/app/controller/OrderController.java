package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.OrderResponse;
import com.bookstore.app.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
    OrderService orderService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll() {
        log.info("Get all orders");
        return ResponseEntity.ok(
                ApiResponse.<List<OrderResponse>>builder()
                        .success(true)
                        .message("Get all orders")
                        .data(orderService.getAll())
                        .build()
        );
    }
}
