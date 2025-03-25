package com.bookstore.app.repository;

import com.bookstore.app.entity.Order;
import com.bookstore.app.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Optional<OrderDetail> findByOrderDetailId(Long orderDetailId);

    Optional<List<OrderDetail>> findByOrder(Order order);
}
