package com.example.chatservice.listener;

import com.example.ConnectionStatus;
import com.example.chatservice.dto.request.UserPrincipalRequest;
import com.example.chatservice.repository.httpclient.ProfileClient;
import com.example.request.ConnectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final ProfileClient profileClient;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Authentication authentication =
                (Authentication) accessor.getUser();

        if (authentication == null) {
            return;
        }

        UserPrincipalRequest principal =
                (UserPrincipalRequest) authentication.getPrincipal();

        if (principal == null) {
            return;
        }

        Long userId = principal.getUserId();
        String userName = principal.getUserName();

        profileClient.changeConnectStatus(userId, ConnectRequest.builder().status(ConnectionStatus.ONLINE).build());

        log.info("User connected: {}", userName);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Authentication authentication =
                (Authentication) accessor.getUser();

        if (authentication == null) {
            return;
        }

        UserPrincipalRequest principal =
                (UserPrincipalRequest) authentication.getPrincipal();

        if (principal == null) {
            return;
        }

        Long userId = principal.getUserId();
        String userName = principal.getUserName();

        profileClient.changeConnectStatus(userId, ConnectRequest.builder().status(ConnectionStatus.OFFLINE).build());

        log.info("User disconnected: {}", userName);
    }
}
