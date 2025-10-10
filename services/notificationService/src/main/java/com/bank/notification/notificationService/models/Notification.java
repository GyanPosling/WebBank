package com.bank.notification.notificationService.models;


import com.bank.notification.notificationService.models.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String email;
    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean sent = false;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}