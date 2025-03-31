package com.bookstore.app.service.impl;

import com.bookstore.app.constant.OrderStatusType;
import com.bookstore.app.dto.request.OrderRequest;
import com.bookstore.app.dto.response.OrderResponse;
import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Order;
import com.bookstore.app.entity.OrderDetail;
import com.bookstore.app.entity.User;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.BookRepository;
import com.bookstore.app.repository.OrderRepository;
import com.bookstore.app.security.CustomUserDetails;
import com.bookstore.app.service.OrderService;
import com.bookstore.app.utils.CartItems;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    BookRepository bookRepository;
    ModelMapper modelMapper;
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    public List<OrderResponse> getOrderByUser(User user) {
        List<Order> orders = orderRepository.findOrderByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No orders found for user: " + user.getUserId()));
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();
    }

    @Override
    public OrderResponse createOrder(String cartId, OrderRequest orderRequest, HttpServletResponse response) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartItems cartItems = (CartItems) redisTemplate.opsForValue().get(cartId);

        if (cartItems == null)
            throw new ResourceNotFoundException("Cart not found with ID: " + cartId);

        Order order = new Order();
        order.setUser(userDetails.getUser());
        setOrderInfo(order, orderRequest);

        Map<Long, Integer> items = cartItems.getItems();
        List<Book> books = bookRepository.findAllById(items.keySet());
        Set<OrderDetail> orderDetails = new HashSet<>();
        double total = 0.0;
        for (Book book : books) {
            long bookId = book.getBookId();
            int quantity = items.get(bookId);
            double subTotal = Math.round(quantity * book.getPrice() * 100.0) / 100.0;
            total += subTotal;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setBook(book);
            orderDetail.setQuantity(quantity);
            orderDetail.setSubTotal(subTotal);
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);
        order.setBookCopies(cartItems.getTotalQuantities());
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);

        Cookie cookie = new Cookie("cartId", null);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        redisTemplate.delete(cartId);

        return modelMapper.map(savedOrder, OrderResponse.class);
    }

    @Override
    public OrderResponse updateInfoOrder(Long orderId, OrderRequest orderRequest) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        setOrderInfo(existingOrder, orderRequest);

        return modelMapper.map(orderRepository.save(existingOrder), OrderResponse.class);
    }

    @Override
    public OrderResponse updateStatusOrder(Long orderId, OrderStatusType orderStatusType) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        existingOrder.setOrderStatus(orderStatusType);
        return modelMapper.map(orderRepository.save(existingOrder), OrderResponse.class);
    }

    @Override
    public void deleteOrderById(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        orderRepository.delete(existingOrder);
    }

    private void setOrderInfo(Order order, OrderRequest orderRequest) {
        order.setRecipientName(orderRequest.getRecipientName());
        order.setRecipientEmail(orderRequest.getRecipientEmail());
        order.setRecipientPhone(orderRequest.getRecipientPhone());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setRecipientCity(orderRequest.getRecipientCity());
        order.setRecipientPostalCode(orderRequest.getRecipientPostalCode());
        order.setRecipientCountry(orderRequest.getRecipientCountry());
        order.setShippingMethod(orderRequest.getShippingMethod());
        order.setShippingCost(orderRequest.getShippingCost());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
    }
}
