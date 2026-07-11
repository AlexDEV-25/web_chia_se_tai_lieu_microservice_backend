package com.example.studyservice.controller.listener;

import com.example.UserProfileUpdatedEvent;
import com.example.studyservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileServiceListener {
    private final DocumentService documentService;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "user-profile-updated", concurrency = "2")
    public void listenUserProfileUpdated(UserProfileUpdatedEvent message) {
        documentService.changeUserInfo(message);
    }

    @KafkaListener(topics = "user-profile-updated.DLT")
    public void listenUserProfileUpdatedDLT(UserProfileUpdatedEvent message) {
        log.error("Message moved to DLT [user-profile-updated.DLT]: {}", message);
    }
}
