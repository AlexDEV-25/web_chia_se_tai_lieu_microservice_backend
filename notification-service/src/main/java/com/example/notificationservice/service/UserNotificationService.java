package com.example.notificationservice.service;


import com.example.UserProfileUpdatedEvent;
import com.example.helper.GetUserIdByToken;
import com.example.notificationservice.dto.request.UserNotificationRequest;
import com.example.notificationservice.dto.response.UserNotificationResponse;
import com.example.notificationservice.mapper.UserNotificationMapper;
import com.example.notificationservice.model.UserNotification;
import com.example.notificationservice.repository.UserNotificationRepository;
import com.example.response.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserNotificationService {
    private final UserNotificationRepository userNotificationRepository;
    private final UserNotificationMapper userNotificationMapper;

    @PreAuthorize("hasAuthority('GET_ALL_USER_NOTIFICATION')")
    public PageResponse<UserNotificationResponse> getByReceiver(int page, int size) {
        Long receiverId = GetUserIdByToken.get();
        Page<UserNotificationResponse> pageData = userNotificationRepository
                .findByReceiverId(receiverId, getPageable(page, size))
                .map(userNotificationMapper::userNotificationToResponse);

        return pageResponse(pageData);
    }

    @PreAuthorize("hasAuthority('GET_UNREAD_USER_NOTIFICATION')")
    public PageResponse<UserNotificationResponse> getByReceiverIdAndReadFalse(int page, int size) {
        Long receiverId = GetUserIdByToken.get();
        Page<UserNotificationResponse> pageData = userNotificationRepository
                .findByReceiverIdAndReadFalse(receiverId, getPageable(page, size))
                .map(userNotificationMapper::userNotificationToResponse);

        return pageResponse(pageData);
    }

    @PreAuthorize("hasAuthority('READ_NOTIFICATION')")
    public UserNotificationResponse read(Long id) {
        Long receiverId = GetUserIdByToken.get();
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

    public void changeSenderInfo(UserProfileUpdatedEvent request) {
        List<UserNotification> userNotifications = userNotificationRepository.findBySenderId(request.getUserId());
        userNotifications.forEach(entity -> {
            entity.setSenderName(request.getFullName());
            userNotificationRepository.save(entity);
        });
    }

    private Pageable getPageable(int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        return PageRequest.of(page - 1, size, sort);
    }

    private PageResponse<UserNotificationResponse> pageResponse(Page<UserNotificationResponse> pageData) {
        return PageResponse.<UserNotificationResponse>builder()
                .currentPage(pageData.getNumber() + 1)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent())
                .build();
    }

}
