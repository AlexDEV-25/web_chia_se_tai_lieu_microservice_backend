package com.example.notificationservice.dto.request;

import com.example.notificationservice.model.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationRequest {

    @NotNull(message = "senderId không được để trống")
    private Long senderId;

    @NotBlank(message = "senderName không được để trống")
    private String senderName;

    @NotNull(message = "receiverId không được để trống")
    private Long receiverId;

    @NotBlank(message = "receiverName không được để trống")
    private String receiverName;

    @NotNull(message = "notificationId không được để trống")
    private Notification notification;

    private boolean read;
}
