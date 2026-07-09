package com.example.notificationservice.controller;

import com.example.event.SystemNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InteractionServiceNotificationController {
    private final BuildNotification buildNotification;

    @KafkaListener(topics = "reply-comment", concurrency = "2")
    public void listenReplyComment(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }
}
