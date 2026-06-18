package com.example.notificationservice.service;

import com.example.notificationservice.dto.request.NotificationRequest;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.mapper.NotificationMapper;
import com.example.notificationservice.model.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public NotificationResponse save(NotificationRequest request) {
        Notification notification = notificationMapper.requestToNotification(request);
        Notification saved = notificationRepository.save(notification);
        return notificationMapper.notificationToResponse(saved);
    }

    public Notification saveNotification(NotificationRequest request) {
        Notification notification = notificationMapper.requestToNotification(request);
        return notificationRepository.save(notification);
    }

}
