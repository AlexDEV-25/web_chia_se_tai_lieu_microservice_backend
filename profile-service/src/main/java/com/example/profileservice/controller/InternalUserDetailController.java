package com.example.profileservice.controller;

import com.example.profileservice.dto.request.UserDetailRequest;
import com.example.profileservice.dto.response.APIResponse;
import com.example.profileservice.dto.response.DisplayRequest;
import com.example.profileservice.dto.response.UserDetailInfoResponse;
import com.example.profileservice.dto.response.UserDetailResponse;
import com.example.profileservice.service.UserDetailService;
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

    @PutMapping(value = "/my-detail-info/{userId}")
    public APIResponse<UserDetailResponse> hideUserDetail(@PathVariable Long userId, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.hideUserDetail(userId, dto));
        return apiResponse;
    }

    @GetMapping("/my-detail-info/{userId}")
    public APIResponse<UserDetailInfoResponse> getUserDetail(@PathVariable Long userId) {
        APIResponse<UserDetailInfoResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.getUserDetailInfo(userId));
        return apiResponse;
    }
}
