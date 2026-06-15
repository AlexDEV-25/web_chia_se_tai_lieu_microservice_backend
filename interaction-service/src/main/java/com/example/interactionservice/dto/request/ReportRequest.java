package com.example.interactionservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {

    @NotNull(message = "documentId không được để trống")
    private Long documentId;

    @NotBlank(message = "lý do không được để trống")
    private String reason;

    @NotBlank(message = "contentTitle không được để trống")
    private String contentTitle;
}
