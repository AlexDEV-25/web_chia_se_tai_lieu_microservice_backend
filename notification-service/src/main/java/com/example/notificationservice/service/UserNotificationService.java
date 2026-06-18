package com.example.notificationservice.service;


import com.example.notificationservice.dto.request.UserNotificationRequest;
import com.example.notificationservice.dto.response.UserNotificationResponse;
import com.example.notificationservice.helper.GetUserIdByToken;
import com.example.notificationservice.mapper.UserNotificationMapper;
import com.example.notificationservice.model.UserNotification;
import com.example.notificationservice.repository.UserNotificationRepository;
import com.example.notificationservice.repository.httpclient.ProfileClient;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserNotificationService {
    private final UserNotificationRepository userNotificationRepository;
    private final UserNotificationMapper userNotificationMapper;
    private final GetUserIdByToken getUserIdByToken;
    private final ProfileClient profileClient;

    @PreAuthorize("hasAuthority('GET_ALL_USER_NOTIFICATION')")
    public List<UserNotificationResponse> getByReceiver() {
        Long receiverId = getUserIdByToken.get();
        List<UserNotification> userNotifications = userNotificationRepository.findByReceiverId(receiverId);
        List<UserNotificationResponse> response = new ArrayList<UserNotificationResponse>();
        for (UserNotification un : userNotifications) {
            response.add(userNotificationMapper.userNotificationToResponse(un));
        }
        return response;
    }

    @PreAuthorize("hasAuthority('GET_UNREAD_USER_NOTIFICATION')")
    public List<UserNotificationResponse> getByReceiverIdAndReadFalse() {
        Long receiverId = getUserIdByToken.get();
        List<UserNotification> userNotifications = userNotificationRepository
                .findByReceiverIdAndReadFalse(receiverId);
        List<UserNotificationResponse> response = new ArrayList<UserNotificationResponse>();
        for (UserNotification un : userNotifications) {
            response.add(userNotificationMapper.userNotificationToResponse(un));
        }
        return response;
    }

    @PreAuthorize("hasAuthority('READ_NOTIFICATION')")
    public UserNotificationResponse read(Long id) {
        Long receiverId = getUserIdByToken.get();
        UserNotification entity = userNotificationRepository.findByIdAndReceiverIdAndReadFalse(id, receiverId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));
        entity.setRead(true);
        UserNotification saved = userNotificationRepository.save(entity);
        return userNotificationMapper.userNotificationToResponse(saved);
    }

    @PreAuthorize("hasAuthority('READ_ALL_NOTIFICATION')")
    public void readAll(Long id) {
        List<UserNotification> entity = userNotificationRepository.findByReceiverIdAndReadFalse(id);
        for (UserNotification userNotification : entity) {
            userNotification.setRead(true);
            userNotificationRepository.save(userNotification);
        }
    }

    public void saveUserNotification(UserNotificationRequest request) {
        UserNotification userNotification = userNotificationMapper.requestToUserNotification(request);
        userNotification.setCreatedAt(LocalDateTime.now());
        userNotificationRepository.save(userNotification);
    }

}
