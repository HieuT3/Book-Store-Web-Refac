package com.bookstore.app.repository;

import com.bookstore.app.entity.Notification;
import com.bookstore.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<List<Notification>> findByUser(User user);
}
