package com.bookstore.app.controller;

import com.bookstore.app.constant.OrderStatusType;
import com.bookstore.app.dto.request.OrderRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.OrderResponse;
import com.bookstore.app.entity.User;
import com.bookstore.app.security.CustomUserDetails;
import com.bookstore.app.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable("orderId") Long orderId) {
        log.info("Get order by id: {}", orderId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Get order by id: " + orderId)
                        .data(orderService.getOrderById(orderId))
                        .build()
        );
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        User user =  userDetails.getUser();
        log.info("Get order by user: {}", user.getUserId());
        return ResponseEntity.ok(
                ApiResponse.<List<OrderResponse>>builder()
                        .success(true)
                        .message("Get order by user: " + user.getUserId() + "successfully!")
                        .data(orderService.getOrderByUser(user))
                        .build()
        );
    }

    @PostMapping("/place-order")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @CookieValue(value = "cartId", defaultValue = "") String cartId,
            @Valid @RequestBody OrderRequest orderRequest,
            HttpServletResponse response) {
        System.out.println(orderRequest);
        log.info("Place order with cartId: {}", cartId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Place order with cartId: " + cartId)
                        .data(orderService.createOrder(cartId, orderRequest, response))
                        .build()
        );
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderRequest orderRequest) {
        log.info("Update order with id: {}", orderId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Update order with id: " + orderId)
                        .data(orderService.updateInfoOrder(orderId, orderRequest))
                        .build()
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("status") OrderStatusType status) {
        log.info("Update order status with id: {}", orderId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .success(true)
                        .message("Update order status with id: " + orderId)
                        .data(orderService.updateStatusOrder(orderId, status))
                        .build()
        );
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable("orderId") Long orderId) {
        log.info("Delete order with id: {}", orderId);
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete order with id: " + orderId)
                        .build()
        );
    }
}
