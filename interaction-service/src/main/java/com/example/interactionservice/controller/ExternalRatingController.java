package com.example.interactionservice.controller;

import com.example.interactionservice.dto.request.RatingRequest;
import com.example.interactionservice.dto.response.RatingAdminProjection;
import com.example.interactionservice.dto.response.RatingDetailAdminProjection;
import com.example.interactionservice.dto.response.RatingUserResponse;
import com.example.interactionservice.service.RatingService;
import com.example.response.APIResponse;
import com.example.response.RatingSummaryResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/ratings")
@AllArgsConstructor
public class ExternalRatingController {
    private final RatingService ratingService;

    @GetMapping("/document-summary/{documentId}")
    public APIResponse<RatingSummaryResponse> getRatingSummaryDocument(@PathVariable Long documentId) {
        APIResponse<RatingSummaryResponse> apiResponse = new APIResponse<RatingSummaryResponse>();
        apiResponse.setResult(ratingService.getRatingSummaryDocument(documentId));
        return apiResponse;
    }

    @GetMapping("/document/my-rating/{documentId}")
    public APIResponse<Integer> getMyRatingDocument(@PathVariable Long documentId) {
        APIResponse<Integer> apiResponse = new APIResponse<Integer>();
        apiResponse.setResult(ratingService.getMyRatingDocument(documentId));
        return apiResponse;
    }

    @PostMapping
    public APIResponse<RatingUserResponse> createRating(@RequestBody @Valid RatingRequest dto) {
        APIResponse<RatingUserResponse> apiResponse = new APIResponse<RatingUserResponse>();
        apiResponse.setResult(ratingService.saveRating(dto));
        return apiResponse;
    }

    @GetMapping("/admin/document/{docId}")
    public APIResponse<RatingDetailAdminProjection> getByDocument(@PathVariable Long docId) {
        APIResponse<RatingDetailAdminProjection> apiResponse = new APIResponse<>();
        apiResponse.setResult(ratingService.getByDocument(docId));
        return apiResponse;
    }

    @GetMapping("/admin/document")
    public APIResponse<RatingAdminProjection> getAllDocumentRatingSummary() {
        APIResponse<RatingAdminProjection> apiResponse = new APIResponse<>();
        apiResponse.setResultList(ratingService.getAllDocumentRatingSummary());
        return apiResponse;
    }

}
