package com.service.notificationservice.service;

import com.service.notificationservice.entity.Notification;
import com.service.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByCustomerId(Long customerId) {
        return notificationRepository.findByCustomerId(customerId);
    }

    public List<Notification> getNotificationsByStatus(Notification.NotificationStatus status) {
        return notificationRepository.findByStatus(status);
    }

    public Notification sendNotification(Notification notification) {
        notification.setStatus(Notification.NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public Notification sendTransactionNotification(Long customerId, String transactionRef, String message) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setTransactionRef(transactionRef);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.TRANSACTION_SUCCESS);
        notification.setStatus(Notification.NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public Notification sendInsufficientBalanceAlert(Long customerId, String message) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.INSUFFICIENT_BALANCE);
        notification.setStatus(Notification.NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public Notification sendLoginAlert(Long customerId, String message) {
        Notification notification = new Notification();
        notification.setCustomerId(customerId);
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.LOGIN_ALERT);
        notification.setStatus(Notification.NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
}
