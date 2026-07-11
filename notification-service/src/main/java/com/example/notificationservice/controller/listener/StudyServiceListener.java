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
public class StudyServiceListener {
    private final BuildNotification buildNotification;

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "admin-delete-document-to-author", concurrency = "2")
    public void listenAdminDeleteDocumentToAuthor(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "admin-delete-document-to-follower", concurrency = "2")
    public void listenAdminDeleteDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "admin-approve-document-to-author", concurrency = "2")
    public void listenAdminApproveDocumentToAuthor(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "admin-approve-document-to-follower", concurrency = "2")
    public void listenAdminApproveDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "admin-hide-document-to-author", concurrency = "2")
    public void listenAdminHideDocumentToAuthor(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "admin-hide-document-to-follower", concurrency = "2")
    public void listenAdminHideDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "author-hide-document-to-follower", concurrency = "2")
    public void listenAuthorHideDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @RetryableTopic(
            attempts = "3",
            dltTopicSuffix = ".DLT"
    )
    @KafkaListener(topics = "author-delete-document-to-follower", concurrency = "2")
    public void listenAuthorDeleteDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @KafkaListener(topics = "admin-delete-document-to-author.DLT")
    public void listenAdminDeleteDocumentToAuthorDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [admin-delete-document-to-author.DLT]: {}", message);
    }

    @KafkaListener(topics = "admin-delete-document-to-follower.DLT")
    public void listenAdminDeleteDocumentToFollowerDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [admin-delete-document-to-follower.DLT]: {}", message);
    }

    @KafkaListener(topics = "admin-approve-document-to-author.DLT")
    public void listenAdminApproveDocumentToAuthorDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [admin-approve-document-to-author.DLT]: {}", message);
    }

    @KafkaListener(topics = "admin-approve-document-to-follower.DLT")
    public void listenAdminApproveDocumentToFollowerDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [admin-approve-document-to-follower.DLT]: {}", message);
    }

    @KafkaListener(topics = "admin-hide-document-to-author.DLT")
    public void listenAdminHideDocumentToAuthorDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [admin-hide-document-to-author.DLT]: {}", message);
    }

    @KafkaListener(topics = "admin-hide-document-to-follower.DLT")
    public void listenAdminHideDocumentToFollowerDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [admin-hide-document-to-follower.DLT]: {}", message);
    }

    @KafkaListener(topics = "author-hide-document-to-follower.DLT")
    public void listenAuthorHideDocumentToFollowerDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [author-hide-document-to-follower.DLT]: {}", message);
    }

    @KafkaListener(topics = "author-delete-document-to-follower.DLT")
    public void listenAuthorDeleteDocumentToFollowerDLT(SystemNotificationEvent message) {
        log.error("Message moved to DLT [author-delete-document-to-follower.DLT]: {}", message);
    }
}
