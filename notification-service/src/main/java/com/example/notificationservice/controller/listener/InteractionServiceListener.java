package com.example.notificationservice.controller.listener;

import com.example.SystemNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InteractionServiceListener {
    private final BuildNotification buildNotification;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "reply-comment", concurrency = "2")
    public void listenReplyComment(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @KafkaListener(topics = "reply-comment.DLT")
    public void listenReplyCommentDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [reply-comment.DLT]: {}", message);
    }
}
