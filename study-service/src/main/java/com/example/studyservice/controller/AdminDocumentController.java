package com.example.studyservice.controller;


import com.example.studyservice.dto.request.DocumentRequest;

import com.example.studyservice.dto.response.APIResponse;
import com.example.studyservice.dto.response.DocumentAdminResponse;
import com.example.studyservice.dto.response.DocumentDetailResponse;
import com.example.studyservice.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents/admin")
@AllArgsConstructor
public class AdminDocumentController {
    private final DocumentService documentService;

    @GetMapping("/{id}")
    public APIResponse<DocumentDetailResponse> getById(@PathVariable Long id) {
        APIResponse<DocumentDetailResponse> apiResponse = new APIResponse<DocumentDetailResponse>();
        apiResponse.setResult(documentService.findById(id));
        return apiResponse;
    }

    @GetMapping
    public APIResponse<DocumentAdminResponse> getAll() {
        APIResponse<DocumentAdminResponse> apiResponse = new APIResponse<DocumentAdminResponse>();
        apiResponse.setResultList(documentService.findAll());
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return new APIResponse<Void>();
    }

    @PutMapping("/{id}")
    public APIResponse<DocumentDetailResponse> update(@PathVariable Long id, @RequestBody DocumentRequest dto) {
        APIResponse<DocumentDetailResponse> apiResponse = new APIResponse<DocumentDetailResponse>();
        apiResponse.setResult(documentService.update(id, dto));
        return apiResponse;
    }

}
