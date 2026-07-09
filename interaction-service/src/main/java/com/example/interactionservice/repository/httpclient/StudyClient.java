package com.example.interactionservice.repository.httpclient;


import com.example.commondto.response.APIResponse;
import com.example.commondto.response.DocumentInfoResponse;
import com.example.commonsecurity.configuration.CommonFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "study-service", url = "${app.services.study.url}", configuration = CommonFeignConfiguration.class)
public interface StudyClient {
    @GetMapping(value = "/api/documents/internal/info/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    APIResponse<DocumentInfoResponse> getAllPublicDocumentsForInteraction(@PathVariable Long documentId);

}
