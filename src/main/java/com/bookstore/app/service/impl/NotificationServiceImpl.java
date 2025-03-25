package com.bookstore.app.service.impl;

import com.bookstore.app.dto.response.NotificationMessage;
import com.bookstore.app.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendNotification(String message) {
        NotificationMessage notificationMessage = NotificationMessage.builder()
                        .message(message)
                                .build();
        simpMessagingTemplate.convertAndSend("/topic/notifications", notificationMessage);
    }
}
