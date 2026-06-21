package com.example.chatservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureResponse {
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private Long userId;
    private String authorName;
    private Long viewsCount;
    private Long downloadsCount;
    private String categoryName;
    private Double average;
    private Long total;
}
