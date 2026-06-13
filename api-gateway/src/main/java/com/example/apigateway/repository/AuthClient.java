package com.example.apigateway.repository;

import com.example.apigateway.dto.request.TokenRequest;
import com.example.apigateway.dto.response.APIResponse;
import com.example.apigateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {
    @PostExchange(url = "/api/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<APIResponse<IntrospectResponse>> introspect(@RequestBody TokenRequest request);
}
