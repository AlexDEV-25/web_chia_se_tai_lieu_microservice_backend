package com.example.studyservice.configuration;

import com.example.configuration.CommonSecurityConfiguration;
import com.example.helper.CustomAuthEntryPoint;
import com.example.helper.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Import(CommonSecurityConfiguration.class)
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS_PUT = {
//			"/api/documents"
            "/api/external/documents/view/{id}",
    };

    private final String[] PUBLIC_ENDPOINTS_GET = {
            "/api/external/categories",

            "/api/external/documents",
            "/api/external/documents/{id}",
            "/api/external/documents/user/{userId}",
            "/api/external/documents/category/{categoryId}",
            "/api/external/documents/user/{userId}",
            "/api/external/documents/count/{userId}",
//            "/api/internal/documents/ai",
//            "/api/internal/documents/info/{documentId}",
//            "/api/internal/documents/last-7-days",
//            "/api/internal/documents/by-category",
    };

    private final CustomJwtDecoder customJwtDecoder;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> //
                request.requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS_PUT).permitAll()//
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()//
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(new CustomAuthEntryPoint()));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}