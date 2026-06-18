package com.example.chatservice.configuration;

import com.example.chatservice.dto.request.UserPrincipalRequest;
import com.example.chatservice.helper.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtHelper jwtHelper;
    private final JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {

                return message;
            }

            try {
                String token = authHeader.replace("Bearer ", "");
                if (!jwtHelper.introspect(token).isValid()) {
                    return null;
                }

                Jwt jwt = jwtDecoder.decode(token);

                String username = jwt.getSubject();
                Long userId = jwt.getClaim("userId");

                String scope = jwt.getClaimAsString("scope");

                UserPrincipalRequest principal =
                        UserPrincipalRequest.builder().userId(userId).userName(username).build();


                List<SimpleGrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                        .map(SimpleGrantedAuthority::new).toList();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, null,
                        authorities);

                accessor.setUser(auth);

            } catch (Exception e) {
                log.error("WebSocket authentication failed", e);
                return null;
            }
        }

        return message;
    }

}