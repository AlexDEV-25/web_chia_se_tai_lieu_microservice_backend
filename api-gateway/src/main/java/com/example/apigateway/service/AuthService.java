package com.example.apigateway.service;


import com.example.apigateway.repository.AuthClient;
import com.example.request.TokenRequest;
import com.example.response.APIResponse;
import com.example.response.IntrospectResponse;
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
