package com.example.studyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponse {
    private Long id;
    private LocalDateTime createdAt;
    private Long contentId;
    private String title;
    private String thumbnailUrl;
    private String authorName;
}
