package com.example.notificationservice.mapper;


import com.example.notificationservice.dto.request.NotificationRequest;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "id", ignore = true)
    Notification requestToNotification(NotificationRequest request);

    NotificationResponse notificationToResponse(Notification entity);
}
