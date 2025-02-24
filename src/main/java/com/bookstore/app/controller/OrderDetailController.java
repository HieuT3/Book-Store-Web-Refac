package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.OrderDetailResponse;
import com.bookstore.app.service.OrderDetailService;
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
@RequestMapping("/api/v1/order-detail")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderDetailController {
    OrderDetailService orderDetailService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getAll() {
        log.info("Get all order details");
        return ResponseEntity.ok(
                ApiResponse.<List<OrderDetailResponse>>builder()
                        .success(true)
                        .message("Get all order details")
                        .data(this.orderDetailService.getAll())
                        .build()
        );
    }
}
