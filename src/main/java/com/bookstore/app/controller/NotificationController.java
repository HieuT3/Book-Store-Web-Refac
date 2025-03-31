package com.bookstore.app.controller;

import com.bookstore.app.dto.request.NotificationRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.NotificationResponse;
import com.bookstore.app.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notification")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {

    NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> sendNotification(
            @Valid @RequestBody NotificationRequest notificationRequest) {
        log.info("Sending notification with title: {}", notificationRequest.getTitle());
        notificationService.sendNotification(notificationRequest);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Send notification successfully")
                        .build()
        );
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllNotifications() {
        log.info("Getting all notifications");
        return ResponseEntity.ok(
                ApiResponse.<List<NotificationResponse>>builder()
                        .success(true)
                        .message("Get all notifications successfully")
                        .data(notificationService.getAll())
                        .build()
        );
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(@PathVariable Long notificationId) {
        log.info("Getting notification with id: {}", notificationId);
        return ResponseEntity.ok(
                ApiResponse.<NotificationResponse>builder()
                        .success(true)
                        .message("Get notification successfully")
                        .data(notificationService.getById(notificationId))
                        .build()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByUserId(@PathVariable Long userId) {
        log.info("Getting notifications for user with id: {}", userId);
        return ResponseEntity.ok(
                ApiResponse.<List<NotificationResponse>>builder()
                        .success(true)
                        .message("Get notifications successfully")
                        .data(notificationService.getByUserId(userId))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<NotificationResponse>> addNotification(
            @Valid @RequestBody NotificationRequest notificationRequest
    ) {
        log.info("Adding notification with title: {}", notificationRequest.getTitle());
        return ResponseEntity.ok(
                ApiResponse.<NotificationResponse>builder()
                        .success(true)
                        .message("Add notification successfully")
                        .data(notificationService.addNotification(notificationRequest))
                        .build()
        );
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> updateNotification(
            @PathVariable Long notificationId,
            @Valid @RequestBody NotificationRequest notificationRequest) {
        log.info("Updating notification with id: {}", notificationId);
        return ResponseEntity.ok(
                ApiResponse.<NotificationResponse>builder()
                        .success(true)
                        .message("Update notification successfully")
                        .data(notificationService.updateNotificationById(notificationId, notificationRequest))
                        .build()
        );
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long notificationId) {
        log.info("Deleting notification with id: {}", notificationId);
        notificationService.deleteById(notificationId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete notification successfully")
                        .build()
        );
    }
}
