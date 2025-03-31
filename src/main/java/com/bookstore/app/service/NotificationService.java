package com.bookstore.app.service;

import com.bookstore.app.dto.request.NotificationRequest;
import com.bookstore.app.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void sendNotification(NotificationRequest notificationRequest);
    List<NotificationResponse> getAll();
    NotificationResponse getById(Long notificationId);
    List<NotificationResponse> getByUserId(Long userId);
    NotificationResponse updateNotificationById(Long notificationId, NotificationRequest notificationRequest);
    NotificationResponse addNotification(NotificationRequest notificationRequest);
    void deleteById(Long notificationId);
}
