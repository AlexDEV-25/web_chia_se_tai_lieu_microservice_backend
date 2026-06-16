package com.example.notificationservice.mapper;


import com.example.notificationservice.dto.request.UserNotificationRequest;
import com.example.notificationservice.dto.response.UserNotificationResponse;
import com.example.notificationservice.model.UserNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserNotificationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserNotification requestToUserNotification(UserNotificationRequest request);
	
    @Mapping(source = "notification.id", target = "notificationId")
    @Mapping(source = "notification.content", target = "notificationContent")
    @Mapping(source = "notification.link", target = "notificationLink")
    @Mapping(source = "notification.type", target = "notificationType")
    UserNotificationResponse userNotificationToResponse(UserNotification entity);
}
