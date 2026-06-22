package com.example.fileservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
    private InputStreamResource resource;
    private long length;
    private MediaType mediaType;
    private String fileName;

}
