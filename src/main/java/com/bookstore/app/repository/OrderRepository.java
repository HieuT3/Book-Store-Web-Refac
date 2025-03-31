package com.bookstore.app.repository;

import com.bookstore.app.entity.Order;
import com.bookstore.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(Long orderId);
    Optional<List<Order>> findOrderByUser(User user);
}
