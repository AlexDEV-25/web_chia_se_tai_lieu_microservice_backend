package com.example.chatservice.dto.response;

import com.example.chatservice.constant.ChatRole;
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
public class ParticipantInfoResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private LocalDateTime lastSeen;
    private ChatRole chatRole;
    private ConnectionStatus userStatus;
}
