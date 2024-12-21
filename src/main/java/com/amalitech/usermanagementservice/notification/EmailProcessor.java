package com.amalitech.usermanagementservice.notification;

import com.amalitech.usermanagementservice.services.MailService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
public class EmailProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailProcessor.class);

    private static final String APP_LOGO_IMAGE = "templates/images/logo.png";

    private final MailService mailService;

    private final TemplateEngine htmlTemplateEngine;

    public EmailProcessor(MailService mailService, TemplateEngine htmlTemplateEngine) {
        this.mailService = mailService;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    public void process(Context data, String email, String subject, String template) {
        try {

            data.setVariable("appLogo", APP_LOGO_IMAGE);

            final String htmlContent = this.htmlTemplateEngine.process(template, data);
            this.mailService.sendHtmlEmail(email, subject, htmlContent);

            LOGGER.info("Received {} message: {}", subject, data);

        } catch (RuntimeException exception) {
            LOGGER.error("Failed to processes message: {}", data, exception);

            // Retry mechanism
        } catch (MessagingException | UnsupportedEncodingException e) {

            LOGGER.error(e.getMessage());
        }
    }

}
