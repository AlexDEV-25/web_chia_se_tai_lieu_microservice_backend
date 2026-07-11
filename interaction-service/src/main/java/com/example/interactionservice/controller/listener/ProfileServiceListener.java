package com.example.interactionservice.controller.listener;

import com.example.UserProfileUpdatedEvent;
import com.example.interactionservice.service.CommentService;
import com.example.interactionservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileServiceListener {
    private final CommentService commentService;
    private final ReportService reportService;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "user-profile-updated", concurrency = "2")
    public void listenUserProfileUpdated(UserProfileUpdatedEvent message) {
        commentService.changeUserInfo(message);
        reportService.changeUserInfo(message);
    }

    @KafkaListener(topics = "user-profile-updated.DLT")
    public void listenUserProfileUpdatedDLT(UserProfileUpdatedEvent message) {
        log.error("Message moved to DLT [user-profile-updated.DLT]: {}", message);
    }
}
