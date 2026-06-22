package com.example.notificationservice.controller;

import com.example.commondto.response.APIResponse;
import com.example.notificationservice.dto.response.UserNotificationResponse;
import com.example.notificationservice.service.UserNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/user-notifications")
@AllArgsConstructor
public class ExternalUserNotificationController {
    private final UserNotificationService userNotificationService;

    @GetMapping("/receiver")
    public APIResponse<UserNotificationResponse> getByReceiver() {
        APIResponse<UserNotificationResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(userNotificationService.getByReceiver());
        return apiResponse;
    }

    @GetMapping("/receiver/unread")
    public APIResponse<UserNotificationResponse> getByReceiverIdAndReadFalse() {
        APIResponse<UserNotificationResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(userNotificationService.getByReceiverIdAndReadFalse());
        return apiResponse;
    }

    @PutMapping("read/{id}")
    public APIResponse<UserNotificationResponse> read(@PathVariable Long id) {
        APIResponse<UserNotificationResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(userNotificationService.read(id));
        return apiResponse;
    }

    @PutMapping("read-all/{id}")
    public APIResponse<Void> readAll(@PathVariable Long id) {
        userNotificationService.readAll(id);
        return APIResponse.<Void>builder().build();
    }
}
