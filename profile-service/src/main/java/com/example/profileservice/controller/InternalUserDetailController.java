package com.example.profileservice.controller;

import com.example.profileservice.service.UserDetailService;
import com.example.request.ConnectRequest;
import com.example.request.DisplayRequest;
import com.example.request.UserDetailRequest;
import com.example.response.APIResponse;
import com.example.response.UserDetailInfoResponse;
import com.example.response.UserDetailResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/users-detail")
@AllArgsConstructor
public class InternalUserDetailController {
    private final UserDetailService userService;

    @PostMapping(value = "/my-detail-info")
    public APIResponse<UserDetailResponse> createUserDetail(@RequestBody @Valid UserDetailRequest dto) {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.createUserDetail(dto));
        return apiResponse;
    }

    @PutMapping(value = "/admin/detail-info/{userId}")
    public APIResponse<UserDetailResponse> hideUserDetail(@PathVariable Long userId, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.hideUserDetail(userId, dto));
        return apiResponse;
    }

    @GetMapping("/detail-info/{userId}")
    public APIResponse<UserDetailInfoResponse> getUserDetail(@PathVariable Long userId) {
        APIResponse<UserDetailInfoResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.getUserDetailInfo(userId));
        return apiResponse;
    }

    @PutMapping(value = "/detail-info/{userId}")
    public APIResponse<Void> changeConnectStatus(@PathVariable Long userId, @RequestBody @Valid ConnectRequest dto) {
        userService.changeConnectStatus(userId, dto.getStatus());
        return new APIResponse<Void>();
    }
}
