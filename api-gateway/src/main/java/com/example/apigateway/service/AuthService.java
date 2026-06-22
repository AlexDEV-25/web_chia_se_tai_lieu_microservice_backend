package com.example.apigateway.service;


import com.example.apigateway.repository.AuthClient;
import com.example.commondto.request.TokenRequest;
import com.example.commondto.response.APIResponse;
import com.example.commondto.response.IntrospectResponse;
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
