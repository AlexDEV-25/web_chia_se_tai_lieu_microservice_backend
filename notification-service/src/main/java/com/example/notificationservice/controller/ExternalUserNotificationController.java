package com.example.notificationservice.controller;

import com.example.notificationservice.dto.response.UserNotificationResponse;
import com.example.notificationservice.service.UserNotificationService;
import com.example.response.APIResponse;
import com.example.response.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/user-notifications")
@AllArgsConstructor
public class ExternalUserNotificationController {
    private final UserNotificationService userNotificationService;

    @GetMapping("/receiver")
    public PageResponse<UserNotificationResponse> getByReceiver(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return userNotificationService.getByReceiver(page, size);
    }

    @GetMapping("/receiver/unread")
    public PageResponse<UserNotificationResponse> getByReceiverIdAndReadFalse(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userNotificationService.getByReceiverIdAndReadFalse(page, size);
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
