package com.example.notificationservice.controller.listener;

import com.example.SystemNotificationEvent;
import com.example.notificationservice.dto.request.NotificationRequest;
import com.example.notificationservice.dto.request.UserNotificationRequest;
import com.example.notificationservice.model.Notification;
import com.example.notificationservice.repository.httpclient.ProfileClient;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.UserNotificationService;
import com.example.response.UserFollowNotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BuildNotification {
    private final NotificationService notificationService;
    private final UserNotificationService userNotificationService;
    private final ProfileClient profileClient;

    public void buildNotificationToUser(SystemNotificationEvent message) {
        Notification notification = notificationService.saveNotification(
                NotificationRequest.builder()
                        .content(message.getContent())
                        .link(message.getLink())
                        .type(message.getType())
                        .build());

        userNotificationService.saveUserNotification(
                UserNotificationRequest.builder()
                        .senderId(message.getSenderId())
                        .senderName(message.getSenderName())
                        .receiverId(message.getReceiverId())
                        .notification(notification).read(false).build());
    }

    public void buildNotificationToFollower(SystemNotificationEvent message) {

        Notification notification = notificationService.saveNotification(
                NotificationRequest.builder()
                        .content(message.getContent())
                        .link(message.getLink())
                        .type(message.getType())
                        .build());

        List<UserFollowNotificationResponse> listFollower =
                profileClient.getFollowerByUserId(message.getSenderId()).getResultList();

        for (UserFollowNotificationResponse userFollowNotificationResponse : listFollower) {
            userNotificationService.saveUserNotification(
                    UserNotificationRequest.builder()
                            .senderId(message.getSenderId())
                            .senderName(message.getSenderName())
                            .receiverId(userFollowNotificationResponse.getFollowerId())
                            .notification(notification).read(false).build());
        }
    }

}
