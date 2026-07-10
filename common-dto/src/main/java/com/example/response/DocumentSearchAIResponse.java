package com.example.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSearchAIResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long userId;
    private String authorName;
    private Long viewsCount;
    private Long downloadsCount;
    private String categoryName;
}
