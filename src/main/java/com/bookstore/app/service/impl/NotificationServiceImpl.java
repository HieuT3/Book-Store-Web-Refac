package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.NotificationRequest;
import com.bookstore.app.dto.response.NotificationResponse;
import com.bookstore.app.entity.Notification;
import com.bookstore.app.entity.User;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.NotificationRepository;
import com.bookstore.app.repository.UserRepository;
import com.bookstore.app.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    SimpMessagingTemplate simpMessagingTemplate;
    UserRepository userRepository;
    NotificationRepository notificationRepository;
    ModelMapper modelMapper;

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        User user = userRepository.findById(notificationRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + notificationRequest.getUserId()));
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(notificationRequest.getTitle());
        notification.setContent(notificationRequest.getContent());

        Notification savedNotification = notificationRepository.save(notification);
        System.out.println(savedNotification);
        simpMessagingTemplate.convertAndSend("/topic/notifications", savedNotification);
    }

    @Override
    public List<NotificationResponse> getAll() {
        return notificationRepository.findAll()
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationResponse.class))
                .toList();
    }

    @Override
    public NotificationResponse getById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        return modelMapper.map(notification, NotificationResponse.class);
    }

    @Override
    public List<NotificationResponse> getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Notification> notifications = notificationRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No notifications found for user with id: " + userId));
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationResponse.class))
                .toList();
    }

    @Override
    public NotificationResponse updateNotificationById(Long notificationId, NotificationRequest notificationRequest) {
        Notification existingNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        existingNotification.setContent(notificationRequest.getContent());
        existingNotification.setTitle(notificationRequest.getTitle());
        return modelMapper.map(notificationRepository.save(existingNotification), NotificationResponse.class);
    }

    @Override
    public NotificationResponse addNotification(NotificationRequest notificationRequest) {
        User user = userRepository.findById(notificationRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + notificationRequest.getUserId()));
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(notificationRequest.getTitle());
        notification.setContent(notificationRequest.getContent());
        return modelMapper.map(notificationRepository.save(notification), NotificationResponse.class);
    }

    @Override
    public void deleteById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notificationRepository.delete(notification);
    }
}
