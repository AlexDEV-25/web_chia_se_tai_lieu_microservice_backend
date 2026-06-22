package com.example.authservice.controller;


import com.example.authservice.dto.request.ChangeEmailRequest;
import com.example.authservice.dto.request.ChangePasswordRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.response.UserResponse;
import com.example.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/users")
@AllArgsConstructor
public class ExternalUserController {
    private final UserService userService;

    @GetMapping("/my-info")
    public APIResponse<UserResponse> getMyInfo() {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(userService.getMyInfo());
        return apiResponse;
    }

    @PutMapping(value = "/my-info")
    public APIResponse<UserResponse> updateMyInfo(@RequestParam @Valid ChangeEmailRequest dto) {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(userService.updateEmail(dto));
        return apiResponse;
    }

    @PutMapping("/change-password")
    public APIResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        APIResponse<Void> apiResponse = new APIResponse<Void>();
        userService.changePassword(request);
        return apiResponse;
    }

    @PutMapping("/admin/hide/{id}")
    public APIResponse<UserResponse> hideUser(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(userService.hideUser(id, dto));
        return apiResponse;
    }

    @GetMapping("/admin")
    public APIResponse<UserResponse> getAllUsers() {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResultList(userService.getAllUsers());
        return apiResponse;
    }

    @PostMapping("/admin")
    public APIResponse<UserResponse> createUser(@RequestBody @Valid RegisterRequest dto) {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(userService.createUser(dto));
        return apiResponse;
    }
}
