package com.example.profileservice.controller;

import com.example.profileservice.dto.response.APIResponse;
import com.example.profileservice.dto.response.UserBioResponse;
import com.example.profileservice.dto.response.UserDetailResponse;
import com.example.profileservice.service.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/users-detail")
@AllArgsConstructor
public class InternalUserDetailController {
    private final UserDetailService userService;

    @GetMapping("/info/{userId}")
    public APIResponse<UserBioResponse> getInfo(@PathVariable Long userId) {
        APIResponse<UserBioResponse> apiResponse = new APIResponse<UserBioResponse>();
        apiResponse.setResult(userService.getBioUser(userId));
        return apiResponse;
    }

    @GetMapping("/my-info/{userId}")
    public APIResponse<UserDetailResponse> getMyInfo(@PathVariable Long id) {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<UserDetailResponse>();
        apiResponse.setResult(userService.getDetailUser(id));
        return apiResponse;
    }

    @GetMapping("/search")
    public APIResponse<UserBioResponse> search(@RequestParam String keyword) {
        APIResponse<UserBioResponse> apiResponse = new APIResponse<UserBioResponse>();
        apiResponse.setResultList(userService.search(keyword));
        return apiResponse;
    }

}
