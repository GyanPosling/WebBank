package com.bank.notification.notificationService.services;


import com.bank.notification.notificationService.dto.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaNotificationListener {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "transaction-events", groupId = "notification-group")
    public void listenTransactionEvents(TransactionEvent event) {
        log.info("Received transaction event: {}", event.getTransactionId());

        String message = String.format(
                "Transaction %s completed. Amount: %s, From: %s, To: %s",
                event.getTransactionId(),
                event.getAmount(),
                event.getFromAccountNumber(),
                event.getToAccountNumber()
        );

        notificationService.sendTransactionNotification(
                "user@example.com",
                message,
                1L
        );
    }
}