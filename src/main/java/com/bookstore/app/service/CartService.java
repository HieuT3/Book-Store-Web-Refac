package com.bookstore.app.service;

import com.bookstore.app.dto.response.CartResponse;

public interface CartService {
    CartResponse getItems(String cartId);
    String addItems(String cartId, Long bookId, int quantity);
    void updateItems(String cartId, Long bookId, int quantity);
    void removeItem(String cartId, Long bookId);
    int getTotalItems(String cartId);
    int getTotalQuantities(String cartId);
    void clearItems(String cartId);

}
