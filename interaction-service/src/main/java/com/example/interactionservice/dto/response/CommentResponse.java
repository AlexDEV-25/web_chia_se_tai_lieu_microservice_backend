package com.example.interactionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long parentId;
    private LocalDateTime updatedAt;
    private Long userId;
    private String username;
    private String userAvatar;
    private Long documentId;
    private Long level;
    private boolean hide;
}
