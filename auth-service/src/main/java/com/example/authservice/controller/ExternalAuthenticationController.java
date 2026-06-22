package com.example.authservice.controller;


import com.example.authservice.dto.request.ActiveAccountRequest;
import com.example.authservice.dto.request.AuthenticationRequest;
import com.example.authservice.dto.request.ForgotPasswordRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.response.AuthenticationResponse;
import com.example.authservice.dto.response.UserResponse;
import com.example.authservice.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/auth")
@AllArgsConstructor
public class ExternalAuthenticationController {
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
    APIResponse<AuthenticationResponse> refreshToken(@RequestBody TokenRequest dto) {
        APIResponse<AuthenticationResponse> apiResponse = new APIResponse<AuthenticationResponse>();
        apiResponse.setResult(authenticationService.refreshToken(dto.getToken()));
        return apiResponse;
    }

    @PostMapping("/introspect")
    APIResponse<IntrospectResponse> introspect(@RequestBody TokenRequest dto) {
        APIResponse<IntrospectResponse> apiResponse = new APIResponse<IntrospectResponse>();
        apiResponse.setResult(authenticationService.introspect(dto.getToken()));
        return apiResponse;
    }
}
