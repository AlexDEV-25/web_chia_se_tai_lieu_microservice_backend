package com.example.apigateway.configuration;

import com.example.apigateway.dto.response.APIResponse;
import com.example.apigateway.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    AuthService authService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicPostEndpoints = {
//			"/api/auth"
            "/api/auth/register", "/api/auth/log-in", "/api/auth/log-in-google", "/api/auth/introspect",
            "/api/auth/refresh-token", "/api/auth/activate", "/api/auth/forgot-password", "/api/auth/change-password",

//			"/api/documents"
            "/api/documents/view/{id}",
    };

    @NonFinal
    private String[] publicGetEndpoints = {
//			"/api/categories"
            "/api/categories",

            "/api/external/users-detail/bio-info/{userId}",


            "/api/external/follows/follow-count/{userId}",

//			"/api/documents"
            "/api/documents", "/api/documents/{id}", "/api/documents/user/{userId}",
            "/api/documents/category/{categoryId}", "/api/documents/user/{userId}", "/api/documents/count/{userId}",

//            "/api/comments"
            "/api/comments/document/{docId}",

//            "/api/ratings"
            "/api/ratings/document-summary/{documentId}",
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (isPublicEndpoint(exchange.getRequest(), exchange.getRequest().getMethod()))
            return chain.filter(exchange);

        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader))
            return unauthenticated(exchange.getResponse());

        String token = authHeader.getFirst().replace("Bearer ", "");

        return authService.introspect(token).flatMap(introspectResponse -> {
            if (introspectResponse.getResult().isValid())
                return chain.filter(exchange);
            else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request, HttpMethod method) {
        if (method.equals(HttpMethod.GET)) {
            return Arrays.stream(publicGetEndpoints)
                    .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));

        } else if (method.equals(HttpMethod.POST)) {
            return Arrays.stream(publicPostEndpoints)
                    .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
        } else return false;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        APIResponse<?> apiResponse = APIResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}