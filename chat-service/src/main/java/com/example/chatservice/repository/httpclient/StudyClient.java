package com.example.chatservice.repository.httpclient;

import com.example.chatservice.dto.response.APIResponse;
import com.example.chatservice.dto.response.CategoryResponse;
import com.example.chatservice.dto.response.DocumentSearchAIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "study-service", url = "${app.services.study.url}")
public interface StudyClient {
    @GetMapping(value = "/api/external/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<CategoryResponse> getAllPublicCategories();

    @GetMapping(value = "/api/internal/documents/ai", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<DocumentSearchAIResponse> getAllPublicDocumentsForAI();

}
