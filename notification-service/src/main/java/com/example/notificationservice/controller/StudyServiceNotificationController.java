package com.example.notificationservice.controller;

import com.example.event.SystemNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudyServiceNotificationController {
    private final BuildNotification buildNotification;

    @KafkaListener(topics = "admin-delete-document-to-author")
    public void listenAdminDeleteDocumentToAuthor(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @KafkaListener(topics = "admin-delete-document-to-follower")
    public void listenAdminDeleteDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @KafkaListener(topics = "admin-approve-document-to-author")
    public void listenAdminApproveDocumentToAuthor(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @KafkaListener(topics = "admin-approve-document-to-follower")
    public void listenAdminApproveDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @KafkaListener(topics = "admin-hide-document-to-author")
    public void listenAdminHideDocumentToAuthor(SystemNotificationEvent message) {
        buildNotification.buildNotificationToUser(message);
    }

    @KafkaListener(topics = "admin-hide-document-to-follower")
    public void listenAdminHideDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @KafkaListener(topics = "author-hide-document-to-follower")
    public void listenAuthorHideDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

    @KafkaListener(topics = "author-delete-document-to-follower")
    public void listenAuthorDeleteDocumentToFollower(SystemNotificationEvent message) {
        buildNotification.buildNotificationToFollower(message);
    }

}
