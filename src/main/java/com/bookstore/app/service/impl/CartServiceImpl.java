package com.bookstore.app.service.impl;

import com.bookstore.app.dto.response.BookResponse;
import com.bookstore.app.dto.response.CartItemResponse;
import com.bookstore.app.dto.response.CartResponse;
import com.bookstore.app.entity.Book;
import com.bookstore.app.repository.BookRepository;
import com.bookstore.app.service.CartService;
import com.bookstore.app.utils.CartItems;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    RedisTemplate<String, Object> redisTemplate;
    BookRepository bookRepository;
    static String CART_KEY_PREFIX = "cart::";
    static Long CART_EXPIRE_TIME = 60 * 60 *  24L;
    Random random = new Random();
    ModelMapper modelMapper;

    private String generateCartId() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 8; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void save(String cartId, CartItems cartItems) {
        redisTemplate.opsForValue().set(cartId, cartItems);
        redisTemplate.expire(cartId, CART_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public CartResponse getItems(String cartId) {
        if (cartId == null || cartId.isEmpty()) {
            return new CartResponse(List.of());
        }

        CartItems cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);
        if (cartItems == null || cartItems.getItems().isEmpty()) {
            return new CartResponse(List.of());
        }

        Map<Long, Integer> items = cartItems.getItems();
        List<Book> books = bookRepository.findAllById(items.keySet());

        return new CartResponse(books.stream()
                .map(book -> new CartItemResponse(
                        modelMapper.map(book, BookResponse.class),
                        items.get(book.getBookId())
                ))
                .toList());
    }

    @Override
    public String addItems(String cartId, Long bookId, int quantity) {
        CartItems cartItems = null;
        if(cartId == null || cartId.isEmpty()) cartId = CART_KEY_PREFIX + generateCartId();
        else cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);
        cartItems = (cartItems != null) ? cartItems : new CartItems();
        cartItems.addItem(bookId, quantity);
        save(cartId, cartItems);
        return cartId;
    }

    @Override
    public void updateItems(String cartId, Long bookId, int quantity) {
        CartItems cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);
        if (cartItems != null) {
            cartItems.updateItem(bookId, quantity);
            save(cartId, cartItems);
        }
    }

    @Override
    public void removeItem(String cartId, Long bookId) {
        CartItems cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);
        if (cartItems != null) {
            cartItems.removeItem(bookId);
            save(cartId, cartItems);
        }
    }

    @Override
    public int getTotalItems(String cartId) {
        CartItems cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);
        if (cartItems != null) {
            return cartItems.getTotalItems();
        }
        return 0;
    }

    @Override
    public int getTotalQuantities(String cartId) {
        CartItems cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);
        if (cartItems != null) {
            return cartItems.getTotalQuantities();
        }
        return 0;
    }

    @Override
    public void clearItems(String cartId) {
        CartItems cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);
        if (cartItems != null) {
            cartItems.clearItems();
            save(cartId, cartItems);
        }
    }
}
