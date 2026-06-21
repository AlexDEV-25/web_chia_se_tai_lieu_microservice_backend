package com.example.interactionservice.service;


import com.example.interactionservice.constant.AppError;
import com.example.interactionservice.dto.request.RatingRequest;
import com.example.interactionservice.dto.response.*;
import com.example.interactionservice.exception.AppException;
import com.example.interactionservice.helper.GetUserIdByToken;
import com.example.interactionservice.mapper.RatingMapper;
import com.example.interactionservice.model.Rating;
import com.example.interactionservice.repository.RatingRepository;
import com.example.interactionservice.repository.httpclient.StudyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository documentRatingRepository;
    private final RatingMapper ratingMapper;
    private final GetUserIdByToken getUserIdByToken;
    private final StudyClient studyClient;

    @PreAuthorize("hasRole('ADMIN')")
    public RatingDetailAdminResponse getByDocument(Long docId) {
        return documentRatingRepository.getDocumentRatingDetail(docId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RatingAdminResponse> getAllDocumentRatingSummary() {
        return documentRatingRepository.getAllDocumentRatingSummary();
    }

    @PreAuthorize("hasAuthority('GET_MY_DOCUMENT_RATING')")
    public Integer getMyRatingDocument(Long docId) {
        Long userId = getUserIdByToken.get();
        return documentRatingRepository.getMyDocumentRating(docId, userId);
    }

    @PreAuthorize("hasAuthority('POST_RATING')")
    public RatingUserResponse saveRating(RatingRequest request) {
        Long userId = getUserIdByToken.get();
        DocumentInfoResponse doc = studyClient.getAllPublicDocumentsForInteraction(request.getDocumentId()).getResult();

        Rating rating = Rating
                .builder()
                .rating(request.getRating())
                .userId(userId)
                .documentId(request.getDocumentId())
                .documentTitle(doc.getTitle())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();

        if (documentRatingRepository.existsByUserIdAndDocumentId(userId, request.getDocumentId())) {
            throw AppException.builder().appError(AppError.ALREADY_RATED).build();
        }
        Rating saved = documentRatingRepository.save(rating);
        return ratingMapper.documentRatingToRatingResponse(saved);

    }

    public RatingSummaryResponse getRatingSummaryDocument(Long docId) {
        return documentRatingRepository.getDocumentRatingSummary(docId);
    }

}
