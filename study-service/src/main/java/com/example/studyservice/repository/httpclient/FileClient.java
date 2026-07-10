package com.example.studyservice.repository.httpclient;

import com.example.configuration.CommonFeignConfiguration;
import com.example.response.APIResponse;
import com.example.studyservice.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "file-service", url = "${app.services.file.url}", configuration = CommonFeignConfiguration.class)
public interface FileClient {
    @GetMapping(value = "/api/internal/files/thumbnail", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<String> getThumbnail(@RequestParam String publicId);

    @GetMapping(value = "/api/internal/files/download", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<FileResponse> downloadFile(@RequestParam String url);

    @DeleteMapping(value = "/api/internal/files/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<Void> deleteFile(@RequestParam String url);

    @PostMapping(value = "/api/internal/files/upload-file-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    APIResponse<Map<String, Object>> uploadPdf(@RequestPart("file") MultipartFile file);

}

