package com.example.apigateway.configuration;


import com.example.apigateway.service.AuthService;
import com.example.commondto.response.APIResponse;
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
import org.springframework.util.AntPathMatcher;
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
            "/api/external/auth/register", "/api/external/auth/log-in", "/api/external/auth/log-in-google", "/api/external/auth/introspect",
            "/api/external/auth/refresh-token", "/api/external/auth/activate", "/api/external/auth/forgot-password", "/api/external/auth/change-password",

//			"/api/documents"
            "/api/external/documents/view/*",
    };

    @NonFinal
    private String[] publicGetEndpoints = {
//			"/api/categories"
            "/api/external/external",

            "/api/external/users-detail/bio-info/*",


            "/api/external/follows/follow-count/*",

//			"/api/documents"
            "/api/external/documents", "/api/external/documents/*", "/api/external/documents/user/*",
            "/api/external/documents/category/*", "/api/external/documents/user/*", "/api/external/documents/count/*",

//            "/api/comments"
            "/api/external/comments/document/*",

//            "/api/ratings"
            "/api/external/ratings/document-summary/*",
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
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String path = request.getURI().getPath();

        if (method.equals(HttpMethod.GET)) {
            return Arrays.stream(publicGetEndpoints)
                    .anyMatch(endpoint ->
                            pathMatcher.match(apiPrefix + endpoint, path));

        } else if (method.equals(HttpMethod.POST)) {
            return Arrays.stream(publicPostEndpoints)
                    .anyMatch(endpoint ->
                            pathMatcher.match(apiPrefix + endpoint, path));
        }

        return false;
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