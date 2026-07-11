package com.example.notificationservice.controller.listener;

import com.example.SystemNotificationEvent;
import com.example.UserProfileUpdatedEvent;
import com.example.notificationservice.service.UserNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileServiceListener {
    private final BuildNotification buildNotification;
    private final UserNotificationService userNotificationService;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "follow-user", concurrency = "2")
    public void listenFollowUser(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "user-profile-updated", concurrency = "2")
    public void listenUserProfileUpdated(UserProfileUpdatedEvent message) {
        userNotificationService.changeSenderInfo(message);
    }

    @KafkaListener(topics = "reply-comment.DLT")
    public void listenFollowUserDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [follow-user.DLT]: {}", message);
    }

    @KafkaListener(topics = "user-profile-updated.DLT")
    public void listenUserProfileUpdatedDLT(UserProfileUpdatedEvent message) {
        log.error("Message moved to DLT [user-profile-updated.DLT]: {}", message);
    }
}
