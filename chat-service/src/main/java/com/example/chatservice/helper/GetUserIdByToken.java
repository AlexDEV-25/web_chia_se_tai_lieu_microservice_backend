package com.example.chatservice.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserIdByToken {
    public Long get() {

        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null) {
            return 0L;
        }

        Authentication authentication = context.getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()) {
            return 0L;
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();

        Long userId = jwt.getClaim("userId");

        if (userId == null || userId <= 0) {
            return 0L;
        }
        return userId;
    }
}
