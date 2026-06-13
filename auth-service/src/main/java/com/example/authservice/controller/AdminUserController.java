package com.example.authservice.controller;

import com.example.authservice.dto.request.DisplayRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.response.APIResponse;
import com.example.authservice.dto.response.UserResponse;
import com.example.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/admin")
@AllArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @PutMapping("hide/{id}")
    public APIResponse<UserResponse> hide(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(userService.hide(id, dto));
        return apiResponse;
    }

    @GetMapping
    public APIResponse<UserResponse> getAll() {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResultList(userService.getAllUsers());
        return apiResponse;
    }

    @PostMapping
    public APIResponse<UserResponse> create(@RequestBody @Valid RegisterRequest dto) {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(userService.save(dto));
        return apiResponse;
    }

}
