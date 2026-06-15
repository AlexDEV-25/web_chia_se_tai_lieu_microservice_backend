package com.example.studyservice.repository.httpclient;

import com.example.studyservice.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "file-service", url = "${app.services.file}")
public interface FileClient {
    @GetMapping(value = "/api/files/{url}/thumbnail", produces = MediaType.APPLICATION_JSON_VALUE)
    String getThumbnail(@PathVariable String publicId);

    @GetMapping(value = "/api/files/{url}/download", produces = MediaType.APPLICATION_JSON_VALUE)
    FileResponse downloadFile(@PathVariable String url);

    @DeleteMapping(value = "/api/files/{url}", produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteFile(@PathVariable String url);

    @PostMapping(value = "/upload-file-pdf", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadPdf(@RequestPart("file") MultipartFile file);

}

