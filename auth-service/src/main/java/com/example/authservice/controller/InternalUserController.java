package com.example.authservice.controller;


import com.example.authservice.service.UserService;
import com.example.commondto.response.APIResponse;
import com.example.commondto.response.DailyCountProjection;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/users")
@AllArgsConstructor
public class InternalUserController {
    private final UserService userService;

    @GetMapping("/users/last-7-days")
    public APIResponse<DailyCountProjection> userLast7Days() {
        return APIResponse.<DailyCountProjection>builder()
                .resultList(userService.userLast7Days()).build();
    }
}
