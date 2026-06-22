package com.example.studyservice.dto.response;


import com.example.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentEventDTO {
    private Long id;
    private String title;
    private Long userId;
    private String authorName;
    private ContentStatus status;
}
