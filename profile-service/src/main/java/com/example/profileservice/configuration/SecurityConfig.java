package com.example.profileservice.configuration;

import com.example.commonsecurity.configuration.CommonSecurityConfiguration;
import com.example.commonsecurity.helper.CustomAuthEntryPoint;
import com.example.commonsecurity.helper.CustomJwtDecoder;
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

    private final String[] PUBLIC_ENDPOINTS_POST = {
            "/api/internal/users-detail/my-detail-info",
    };

    private final String[] PUBLIC_ENDPOINTS_GET = {
            "/api/external/users-detail/bio-info/{userId}",
            "/api/external/follows/follow-count/{userId}",

            "/api/internal/users-detail/detail-info/{userId}",
            "/api/internal/follows/follower/{userId}"
    };

    private final String[] PUBLIC_ENDPOINTS_PUT = {
            "/api/internal/users-detail/admin/detail-info/{userId}", "/api/internal/users-detail/detail-info/{userId}"
    };


    private final CustomJwtDecoder customJwtDecoder;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> //
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()//
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()//
                        .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINTS_PUT).permitAll()//
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