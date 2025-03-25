package com.bookstore.app.service;

import com.bookstore.app.utils.CartItems;

public interface CartService {
    CartItems getItems(String cartId);
    String addItems(String cartId, Long bookId, int quantity);
    void minusItems(String cartId, Long bookId, int quantity);
    void removeItem(String cartId, Long bookId);
    int getTotalItems(String cartId);
    int getTotalQuantities(String cartId);
    void clearItems(String cartId);

}
