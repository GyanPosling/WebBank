package com.bank.notification.notificationService.repositories;


import com.bank.notification.notificationService.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndSentFalse(Long userId);
    List<Notification> findBySentFalse();
}
