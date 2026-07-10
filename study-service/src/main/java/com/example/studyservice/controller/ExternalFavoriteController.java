package com.example.studyservice.controller;

import com.example.response.APIResponse;
import com.example.studyservice.dto.request.FavoriteRequest;
import com.example.studyservice.dto.response.FavoriteResponse;
import com.example.studyservice.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/favorites")
@AllArgsConstructor
@Slf4j
public class ExternalFavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public APIResponse<FavoriteResponse> addFavorite(@RequestBody @Valid FavoriteRequest dto) {
        APIResponse<FavoriteResponse> apiResponse = new APIResponse<FavoriteResponse>();
        apiResponse.setResult(favoriteService.addFavorite(dto));
        return apiResponse;
    }

    @DeleteMapping("/document/{documentId}")
    public APIResponse<Void> removeDocumentFavorite(@PathVariable Long documentId) {
        favoriteService.removeDocumentFavorite(documentId);
        return APIResponse.<Void>builder().build();
    }

    @GetMapping("/document/user")
    public APIResponse<FavoriteResponse> getDocumentFavoritesByUser() {
        APIResponse<FavoriteResponse> apiResponse = new APIResponse<FavoriteResponse>();
        apiResponse.setResultList(favoriteService.getDocumentFavoritesByUser());
        return apiResponse;
    }

    @GetMapping("/document/user/check/{documentId}")
    public APIResponse<Boolean> checkDocumentFavorite(@PathVariable Long documentId) {
        APIResponse<Boolean> apiResponse = new APIResponse<Boolean>();
        apiResponse.setResult(favoriteService.checkDocumentFavorite(documentId));
        return apiResponse;
    }
}
