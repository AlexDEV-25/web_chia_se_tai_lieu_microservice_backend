package com.example.studyservice.controller;

import com.example.commondto.response.*;
import com.example.studyservice.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/documents")
@AllArgsConstructor
public class InternalDocumentController {
    private final DocumentService documentService;

    @GetMapping("/ai")
    public APIResponse<DocumentSearchAIResponse> getAllPublicDocuments() {
        APIResponse<DocumentSearchAIResponse> apiResponse = new APIResponse<>();
        apiResponse.setResultList(documentService.getAllPublicDocumentsForAI());
        return apiResponse;
    }

    @GetMapping("/info/{documentId}")
    public APIResponse<DocumentInfoResponse> getAllPublicDocumentsForInteraction(@PathVariable Long documentId) {
        APIResponse<DocumentInfoResponse> apiResponse = new APIResponse<>();
        apiResponse.setResult(documentService.getAllPublicDocumentsForInteraction(documentId));
        return apiResponse;
    }


    @GetMapping("/last-7-days")
    public APIResponse<DailyCountProjection> documentLast7Days() {
        return APIResponse.<DailyCountProjection>builder()
                .resultList(documentService.documentLast7Days()).build();
    }

    @GetMapping("/by-category")
    public APIResponse<CategoryCountProjection> documentByCategory() {
        return APIResponse.<CategoryCountProjection>builder()
                .resultList(documentService.documentByCategory()).build();
    }
}
