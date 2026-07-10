package com.example.notificationservice.controller;

import com.example.SystemNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileServiceNotificationController {
    private final BuildNotification buildNotification;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "follow-user", concurrency = "2")
    public void listenFollowUser(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @KafkaListener(topics = "reply-comment.DLT")
    public void listenFollowUserDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [follow-user.DLT]: {}", message);
    }
}
