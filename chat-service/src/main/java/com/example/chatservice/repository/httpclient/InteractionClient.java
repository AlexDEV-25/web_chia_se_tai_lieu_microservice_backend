package com.example.chatservice.repository.httpclient;

import com.example.commondto.request.DisplayRequest;
import com.example.commondto.response.APIResponse;
import com.example.commondto.response.CommentAdminResponse;
import com.example.commondto.response.CommentDetailAdminResponse;
import com.example.commondto.response.RatingSummaryResponse;
import com.example.commonsecurity.configuration.CommonFeignConfiguration;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "interaction-service", url = "${app.services.interaction.url}", configuration = CommonFeignConfiguration.class)
public interface InteractionClient {
    @GetMapping(value = "/api/internal/comments/admin/7-days", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<CommentAdminResponse> findDocumentCommentsLast7Days();

    @PutMapping(value = "/api/external/comments/admin/hide/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<CommentDetailAdminResponse> hide(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto);

    @GetMapping(value = "/api/ratings/document-summary/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<RatingSummaryResponse> getRatingSummaryDocument(@PathVariable Long documentId);
}
