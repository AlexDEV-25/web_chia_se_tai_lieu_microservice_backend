package com.example.studyservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentStatsResponse {
    private long totalDocuments;
    private long totalDownloads;
    private long totalViews;
}
