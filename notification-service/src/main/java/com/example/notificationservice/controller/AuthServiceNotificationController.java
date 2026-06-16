package com.example.notificationservice.controller;

import com.example.event.EmailNotificationEvent;
import com.example.notificationservice.dto.request.Recipient;
import com.example.notificationservice.dto.request.SendEmailRequest;
import com.example.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceNotificationController {
    private final EmailService emailService;

    @KafkaListener(topics = "activate-account")
    public void listenActivateAccount(EmailNotificationEvent message) {
        emailService.sendEmail(
                SendEmailRequest.builder()
                        .to(Recipient.builder()
                                .email(message.getRecipient())
                                .build())
                        .subject(message.getSubject())
                        .htmlContent(message.getBody())
                        .build());
    }

    @KafkaListener(topics = "change-password")
    public void listenChangePassword(EmailNotificationEvent message) {
        emailService.sendEmail(
                SendEmailRequest.builder()
                        .to(Recipient.builder()
                                .email(message.getRecipient())
                                .build())
                        .subject(message.getSubject())
                        .htmlContent(message.getBody())
                        .build());
    }

    @KafkaListener(topics = "lock-account")
    public void listenLockAccount(EmailNotificationEvent message) {
        emailService.sendEmail(
                SendEmailRequest.builder()
                        .to(Recipient.builder()
                                .email(message.getRecipient())
                                .build())
                        .subject(message.getSubject())
                        .htmlContent(message.getBody())
                        .build());
    }
}
