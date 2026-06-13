package com.example.apigateway.service;

import com.example.apigateway.dto.request.TokenRequest;
import com.example.apigateway.dto.response.APIResponse;
import com.example.apigateway.dto.response.IntrospectResponse;
import com.example.apigateway.repository.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthClient authClient;

    public Mono<APIResponse<IntrospectResponse>> introspect(String token) {
        return authClient.introspect(TokenRequest.builder().token(token).build());
    }
}
