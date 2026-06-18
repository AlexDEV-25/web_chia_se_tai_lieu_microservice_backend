package com.example.chatservice.repository.httpclient;

import com.example.chatservice.dto.request.DisplayRequest;
import com.example.chatservice.dto.response.APIResponse;
import com.example.chatservice.dto.response.CommentAdminResponse;
import com.example.chatservice.dto.response.CommentDetailAdminResponse;
import com.example.chatservice.dto.response.RatingSummaryResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "interaction-service", url = "${app.services.interaction.url}")
public interface InteractionClient {
    @GetMapping(value = "/api/internal/comments/admin/7-days", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<CommentAdminResponse> findDocumentCommentsLast7Days();

    @PutMapping(value = "/api/external/comments/admin/hide/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<CommentDetailAdminResponse> hide(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto);

    @GetMapping(value = "/api/ratings/document-summary/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<RatingSummaryResponse> getRatingSummaryDocument(@PathVariable Long documentId);
}
