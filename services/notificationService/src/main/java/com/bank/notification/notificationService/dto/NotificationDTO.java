package com.bank.notification.notificationService.dto;

import com.bank.notification.notificationService.models.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String email;
    private String title;
    private String message;
    private NotificationType type;
    private boolean sent;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
