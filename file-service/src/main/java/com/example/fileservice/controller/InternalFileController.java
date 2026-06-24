package com.example.fileservice.controller;

import com.example.commondto.response.APIResponse;
import com.example.fileservice.dto.response.FileResponse;
import com.example.fileservice.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/internal/files")
@AllArgsConstructor
public class InternalFileController {
    private final FileService fileService;

    @GetMapping("/thumbnail")
    public APIResponse<String> getThumbnail(@RequestParam String publicId) {
        APIResponse<String> apiResponse = new APIResponse<String>();
        apiResponse.setResult(fileService.getThumbnail(publicId));
        return apiResponse;
    }

    @GetMapping("/download")
    public APIResponse<FileResponse> downloadFile(@RequestParam String url) throws Exception {
        FileResponse file = fileService.downloadFile(url);
        APIResponse<FileResponse> apiResponse = new APIResponse<FileResponse>();
        apiResponse.setResult(file);
        return apiResponse;

    }

    @DeleteMapping("/delete")
    public APIResponse<Void> deleteFile(@RequestParam String url) {
        fileService.deleteFile(url);
        return APIResponse.<Void>builder().build();
    }

    @PostMapping(value = "/upload-file-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<Map<String, Object>> uploadPdf(@RequestPart("file") MultipartFile file) {
        APIResponse<Map<String, Object>> apiResponse = new APIResponse<Map<String, Object>>();
        apiResponse.setResult(fileService.uploadPdf(file));
        return apiResponse;

    }

    @PostMapping(value = "/upload-file-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<Map<String, Object>> uploadImage(@RequestPart("file") MultipartFile file) {
        APIResponse<Map<String, Object>> apiResponse = new APIResponse<Map<String, Object>>();
        apiResponse.setResult(fileService.uploadImage(file));
        return apiResponse;

    }
}
