package com.service.notificationservice.controller;

import com.service.notificationservice.dto.AlertNotificationRequest;
import com.service.notificationservice.dto.TransactionNotificationRequest;
import com.service.notificationservice.entity.Notification;
import com.service.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> createNotification(@Valid @RequestBody Notification notification) {
        Notification created = notificationService.createNotification(notification);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Notification>> getNotificationsByCustomerId(@PathVariable Long customerId) {
        List<Notification> notifications = notificationService.getNotificationsByCustomerId(customerId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<Notification> sendNotification(@PathVariable Long id) {
        try {
            Notification notification = notificationService.getNotificationById(id)
                    .orElseThrow(() -> new RuntimeException("Notification not found"));
            Notification sent = notificationService.sendNotification(notification);
            return ResponseEntity.ok(sent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<Notification> sendTransactionNotification(@Valid @RequestBody TransactionNotificationRequest request) {
        Notification notification = notificationService.sendTransactionNotification(
                request.getCustomerId(), 
                request.getTransactionRef(), 
                request.getMessage()
        );
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @PostMapping("/insufficient-balance")
    public ResponseEntity<Notification> sendInsufficientBalanceAlert(@Valid @RequestBody AlertNotificationRequest request) {
        Notification notification = notificationService.sendInsufficientBalanceAlert(
                request.getCustomerId(), 
                request.getMessage()
        );
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @PostMapping("/login-alert")
    public ResponseEntity<Notification> sendLoginAlert(@Valid @RequestBody AlertNotificationRequest request) {
        Notification notification = notificationService.sendLoginAlert(
                request.getCustomerId(), 
                request.getMessage()
        );
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }
}
