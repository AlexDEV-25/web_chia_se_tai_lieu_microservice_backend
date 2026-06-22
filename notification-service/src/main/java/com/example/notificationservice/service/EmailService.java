package com.example.notificationservice.service;

import com.example.AppError;
import com.example.commonexception.exception.AppException;
import com.example.notificationservice.dto.request.EmailRequest;
import com.example.notificationservice.dto.request.SendEmailRequest;
import com.example.notificationservice.dto.request.Sender;
import com.example.notificationservice.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailClient emailClient;

    @Value("${app.email.api-key}")
    @NonFinal
    String apiKey;

    @Value("${app.email.sender-email}")
    @NonFinal
    String senderEmail;

    @Value("${app.email.sender-name}")
    @NonFinal
    String senderName;

    public void sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name(senderName)
                        .email(senderEmail)
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new AppException(AppError.CANNOT_SEND_EMAIL);
        }
    }
}