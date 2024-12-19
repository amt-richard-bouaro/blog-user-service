package com.amalitech.usermanagementservice.notification;

public interface EmailNotification {
    void handlePasswordResetNotification(String email, String username, String newPassword);
}
