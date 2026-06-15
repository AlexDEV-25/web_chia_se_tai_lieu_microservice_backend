package com.example.interactionservice.controller;


import com.example.interactionservice.dto.response.APIResponse;
import com.example.interactionservice.dto.response.RatingAdminResponse;
import com.example.interactionservice.dto.response.RatingDetailAdminResponse;
import com.example.interactionservice.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings/admin")
@AllArgsConstructor
public class AdminRatingController {
    private final RatingService ratingService;

    @GetMapping("/document/{docId}")
    public APIResponse<RatingDetailAdminResponse> getByDocument(@PathVariable Long docId) {
        APIResponse<RatingDetailAdminResponse> apiResponse = new APIResponse<RatingDetailAdminResponse>();
        apiResponse.setResult(ratingService.getByDocument(docId));
        return apiResponse;
    }

    @GetMapping("/document")
    public APIResponse<RatingAdminResponse> getAllDocumentRatingSummary() {
        APIResponse<RatingAdminResponse> apiResponse = new APIResponse<RatingAdminResponse>();
        apiResponse.setResultList(ratingService.getAllDocumentRatingSummary());
        return apiResponse;
    }

}
