package com.amalitech.usermanagementservice.notification;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class EmailNotificationImp implements EmailNotification {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationImp.class);

    private final EmailProcessor emailProcessor;
    private static final String PASSWORD_RESET_TEMPLATE_NAME = "passwordReset";
    private static final String PASSWORD_RESET_SUBJECT = "Password Reset";


    public void handlePasswordResetNotification(String email, String username, String newPassword) {
        try {

            final Context passwordResetData = new Context(LocaleContextHolder.getLocale());

            passwordResetData.setVariable("name", username);
            passwordResetData.setVariable("password", newPassword);

            emailProcessor.process(passwordResetData, email, PASSWORD_RESET_SUBJECT, PASSWORD_RESET_TEMPLATE_NAME);

        } catch (RuntimeException exception) {
            LOGGER.error("Failed to processes message: {}", PASSWORD_RESET_SUBJECT, exception);
        }
    }


}