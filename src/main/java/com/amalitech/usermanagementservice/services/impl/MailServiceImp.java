package com.amalitech.usermanagementservice.services.impl;

import com.amalitech.usermanagementservice.config.properties.EmailPropertiesConfig;
import com.amalitech.usermanagementservice.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailServiceImp implements MailService {

    private final JavaMailSender mailSender;

    private final EmailPropertiesConfig emailPropertiesConfig;
    
    @Override
    public void sendEmail(
            String to,
            String subject,
            String body
    ) {
        
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        
    }
    
    
    @Override
    public void sendHtmlEmail(
            String to,
            String subject,
            String htmlBody
    ) throws MessagingException, UnsupportedEncodingException {
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom(new InternetAddress(emailPropertiesConfig.username(), "JU Blog"));

        mailSender.send(message);
    }
}
