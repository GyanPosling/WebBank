package com.bank.notification.notificationService.services;

import com.bank.notification.notificationService.models.Notification;
import com.bank.notification.notificationService.models.enums.NotificationType;
import com.bank.notification.notificationService.repositories.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSent(false);
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created: {}", savedNotification.getId());
        return savedNotification;
    }

    public void sendTransactionNotification(String email, String message, Long userId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setEmail(email);
        notification.setTitle("Transaction Notification");
        notification.setMessage(message);
        notification.setType(NotificationType.TRANSACTION);

        createNotification(notification);
        sendEmailNotification(notification);
        sendWebSocketNotification(userId, message);
    }

    private void sendEmailNotification(Notification notification) {
        try {
            emailService.sendEmail(
                    notification.getEmail(),
                    notification.getTitle(),
                    notification.getMessage()
            );
            notification.setSent(true);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
            log.info("Email notification sent for notification: {}", notification.getId());
        } catch (Exception e) {
            log.error("Failed to send email notification: {}", e.getMessage());
        }
    }

    private void sendWebSocketNotification(Long userId, String message) {
        try {
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
            log.info("WebSocket notification sent to user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification: {}", e.getMessage());
        }
    }
}
