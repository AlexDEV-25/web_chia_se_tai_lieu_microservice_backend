package com.example.profileservice.dto.response;

import com.example.profileservice.constant.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ConnectionStatus status;
    private boolean hide;
}
