package com.example.profileservice.dto.response;

import com.example.profileservice.constant.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBioResponse {
    private Long id;
    private String fullName;
    private String avatarUrl;
    private String bio;
    private ConnectionStatus status;
}
