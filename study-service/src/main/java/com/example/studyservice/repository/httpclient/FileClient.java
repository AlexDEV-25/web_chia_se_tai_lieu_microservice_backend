package com.example.studyservice.repository.httpclient;

import com.example.commondto.response.APIResponse;
import com.example.studyservice.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "file-service", url = "${app.services.file.url}")
public interface FileClient {
    @GetMapping(value = "/api/internal/files/{url}/thumbnail", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<String> getThumbnail(@PathVariable String publicId);

    @GetMapping(value = "/api/internal/files/{url}/download", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<FileResponse> downloadFile(@PathVariable String url);

    @DeleteMapping(value = "/api/internal/files/{url}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<Void> deleteFile(@PathVariable String url);

    @PostMapping(value = "/api/internal/files/upload-file-pdf", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    APIResponse<Map<String, Object>> uploadPdf(@RequestPart("file") MultipartFile file);

}

