package com.example.notificationservice.controller;

import com.example.event.SystemNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileServiceNotificationController {
    private final BuildNotification buildNotification;

    @KafkaListener(topics = "follow-user", concurrency = "2")
    public void listenFollowUser(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }
}
