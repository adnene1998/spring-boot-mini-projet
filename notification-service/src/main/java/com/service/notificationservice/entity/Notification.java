package com.service.notificationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotBlank(message = "Message is required")
    @Column(length = 1000)
    private String message;
    
    @NotNull(message = "Notification type is required")
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    
    private String transactionRef;
    
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = NotificationStatus.PENDING;
        }
    }
    
    public enum NotificationType {
        TRANSACTION_SUCCESS, INSUFFICIENT_BALANCE, LOGIN_ALERT, ACCOUNT_UPDATE
    }
    
    public enum NotificationStatus {
        PENDING, SENT, FAILED
    }
}
