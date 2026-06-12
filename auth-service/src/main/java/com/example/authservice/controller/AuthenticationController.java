package com.example.authservice.controller;


import com.example.authservice.constant.AppError;
import com.example.authservice.dto.request.ActiveAccountRequest;
import com.example.authservice.dto.request.AuthenticationRequest;
import com.example.authservice.dto.request.ForgotPasswordRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.response.APIResponse;
import com.example.authservice.dto.response.AuthenticationResponse;
import com.example.authservice.dto.response.IntrospectResponse;
import com.example.authservice.dto.response.UserResponse;
import com.example.authservice.exception.AppException;
import com.example.authservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/log-in")
    APIResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest dto) {
        APIResponse<AuthenticationResponse> apiResponse = new APIResponse<AuthenticationResponse>();
        apiResponse.setResult(authenticationService.login(dto));
        return apiResponse;
    }

    @PostMapping("/log-in-google")
    APIResponse<AuthenticationResponse> loginWithGoogle(@RequestParam String code) {
        APIResponse<AuthenticationResponse> apiResponse = new APIResponse<AuthenticationResponse>();
        apiResponse.setResult(authenticationService.loginWithGoogle(code));
        return apiResponse;
    }

    @PostMapping("/register")
    public APIResponse<UserResponse> register(@RequestBody @Valid RegisterRequest dto) {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(authenticationService.register(dto));
        return apiResponse;
    }

    @PostMapping("/activate")
    public APIResponse<Void> activateAccount(@RequestBody ActiveAccountRequest dto) {
        APIResponse<Void> apiResponse = new APIResponse<Void>();
        authenticationService.activateAccount(dto);
        return apiResponse;
    }

    @PostMapping("/forgot-password")
    public APIResponse<Void> forgotPassword(@RequestParam String email) {
        APIResponse<Void> apiResponse = new APIResponse<Void>();
        authenticationService.forgotPassword(email);
        return apiResponse;
    }

    @PostMapping("/change-password")
    public APIResponse<UserResponse> ChangePassword(@RequestBody ForgotPasswordRequest dto) {
        APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
        apiResponse.setResult(authenticationService.changePassword(dto));
        return apiResponse;
    }

    @PostMapping("/refresh-token")
    APIResponse<AuthenticationResponse> refreshToken(HttpServletRequest dto) {
        String oldToken = this.extractTokenFromHeader(dto);
        APIResponse<AuthenticationResponse> apiResponse = new APIResponse<AuthenticationResponse>();
        apiResponse.setResult(authenticationService.refreshToken(oldToken));
        return apiResponse;
    }

    @PostMapping("/introspect")
    APIResponse<IntrospectResponse> introspect(HttpServletRequest dto) {
        String token = this.extractTokenFromHeader(dto);
        APIResponse<IntrospectResponse> apiResponse = new APIResponse<IntrospectResponse>();
        apiResponse.setResult(authenticationService.introspect(token));
        return apiResponse;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw AppException.builder().appError(AppError.MISSING_TOKEN).build();
        }

        return authHeader.substring(7).trim();
    }
}
