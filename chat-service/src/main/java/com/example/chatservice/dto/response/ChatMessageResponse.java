package com.example.chatservice.dto.response;

import com.example.chatservice.constant.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long id;
    private Long conversationId;
    private boolean me;
    private String message;
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private ConnectionStatus userStatus;
    private LocalDateTime createdAt;
}
