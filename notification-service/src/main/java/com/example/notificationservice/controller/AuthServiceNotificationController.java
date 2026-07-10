package com.example.notificationservice.controller;

import com.example.EmailNotificationEvent;
import com.example.notificationservice.dto.request.Recipient;
import com.example.notificationservice.dto.request.SendEmailRequest;
import com.example.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceNotificationController {
    private final EmailService emailService;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "activate-account", concurrency = "2")
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

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "change-password", concurrency = "2")
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

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "lock-account", concurrency = "2")
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

    @KafkaListener(topics = "activate-account.DLT")
    public void listenActivateAccountDLT(EmailNotificationEvent message) {
        log.error("Message moved to DLT [activate-account.DLT]: {}", message);
    }

    @KafkaListener(topics = "change-password.DLT")
    public void listenChangePasswordDLT(EmailNotificationEvent message) {
        log.error("Message moved to DLT [change-password.DLT]: {}", message);
    }

    @KafkaListener(topics = "lock-account.DLT")
    public void listenLockAccountDLT(EmailNotificationEvent message) {
        log.error("Message moved to DLT [lock-account.DLT]: {}", message);
    }

}
