package com.bookstore.app.controller;

import com.bookstore.app.dto.request.CartRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.service.CartService;
import com.bookstore.app.utils.CartItems;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CartController {

    CartService cartService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<CartItems>> getItems(@CookieValue(value = "cartId", defaultValue = "") String cartId) {
        log.info("Get items in cart from cartId: {}", cartId);
        return ResponseEntity.ok(
                ApiResponse.<CartItems>builder()
                        .success(true)
                        .message("Get items in cart successfully with cartId: " + cartId)
                        .data(cartService.getItems(cartId))
                        .build()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addItems(@CookieValue(value = "cartId", defaultValue = "") String cartId,
                                                      @Valid @RequestBody CartRequest cartRequest,
                                                      HttpServletResponse response) {
        log.info("Add items to cart from cartId: {}", cartId);
        String cartIdValue = cartService.addItems(cartId, cartRequest.getBookId(), cartRequest.getQuantity());

        Cookie cookie = new Cookie("cartId", cartIdValue);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Add items to cart successfully with cartId: " + cartId)
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/minus")
    public ResponseEntity<ApiResponse<Void>> minusItems(@CookieValue(value = "cartId", defaultValue = "") String cartId,
                                                         @Valid @RequestBody CartRequest cartRequest) {
        log.info("Minus items in cart from cartId: {}", cartId);
        cartService.minusItems(cartId, cartRequest.getBookId(), cartRequest.getQuantity());
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Minus items in cart successfully with cartId: " + cartId)
                        .data(null)
                        .build()
        );
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Void>> removeItem(@CookieValue(value = "cartId", defaultValue = "") String cartId,
                                                         @RequestParam("bookId") String bookId) {
        log.info("Remove item in cart from cartId: {}", cartId);
        cartService.removeItem(cartId,bookId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Remove item in cart successfully with cartId: " + cartId)
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/total-items")
    public ResponseEntity<ApiResponse<Integer>> getTotalItems(@CookieValue(value = "cartId", defaultValue = "") String cartId) {
        log.info("Get total items in cart from cartId: {}", cartId);
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .success(true)
                        .message("Get total items in cart successfully with cartId: " + cartId)
                        .data(cartService.getTotalItems(cartId))
                        .build()
        );
    }

    @GetMapping("/total-quantities")
    public ResponseEntity<ApiResponse<Integer>> getTotalQuantities(@CookieValue(value = "cartId", defaultValue = "") String cartId) {
        log.info("Get total quantities in cart from cartId: {}", cartId);
        return ResponseEntity.ok(
                ApiResponse.<Integer>builder()
                        .success(true)
                        .message("Get total quantities in cart successfully with cartId: " + cartId)
                        .data(cartService.getTotalQuantities(cartId))
                        .build()
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearItems(@CookieValue(value = "cartId", defaultValue = "") String cartId) {
        log.info("Clear items in cart from cartId: {}", cartId);
        cartService.clearItems(cartId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Clear items in cart successfully with cartId: " + cartId)
                        .data(null)
                        .build()
        );
    }
}
