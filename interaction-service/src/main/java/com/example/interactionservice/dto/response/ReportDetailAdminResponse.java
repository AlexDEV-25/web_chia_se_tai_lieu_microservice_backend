package com.example.interactionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetailAdminResponse {
    private Long id;
    private Long documentId;
    private String documentTitle;
    private String username;
    private String reason;
}
