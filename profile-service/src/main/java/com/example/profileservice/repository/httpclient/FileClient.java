package com.example.profileservice.repository.httpclient;


import com.example.commondto.response.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "file-service", url = "${app.services.file.url}")
public interface FileClient {

    @DeleteMapping(value = "/api/internal/files/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<Void> deleteFile(@RequestParam String url);

    @PostMapping(value = "/api/internal/files/upload-file-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    APIResponse<Map<String, Object>> uploadImage(@RequestPart("file") MultipartFile file);

}

