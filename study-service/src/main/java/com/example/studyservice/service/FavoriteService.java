package com.example.studyservice.service;


import com.example.AppError;
import com.example.ContentStatus;
import com.example.commonexception.exception.AppException;
import com.example.commonsecurity.helper.GetUserIdByToken;
import com.example.studyservice.dto.request.FavoriteRequest;
import com.example.studyservice.dto.response.FavoriteResponse;
import com.example.studyservice.mapper.FavoriteMapper;
import com.example.studyservice.model.Document;
import com.example.studyservice.model.Favorite;
import com.example.studyservice.repository.DocumentRepository;
import com.example.studyservice.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteDocumentRepository;
    private final DocumentRepository documentRepository;
    private final FavoriteMapper favoriteMapper;
    private final GetUserIdByToken getUserIdByToken;

    @PreAuthorize("hasAuthority('ADD_FAVORITE')")
    public FavoriteResponse addFavorite(FavoriteRequest request) {
        Long userId = getUserIdByToken.get();
        Favorite favorite = Favorite.builder().createdAt(LocalDateTime.now()).userId(userId).build();
        Document doc = documentRepository.findById(request.getContentId())
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        favorite.setDocument(doc);
        Favorite saved = favoriteDocumentRepository.save(favorite);
        return favoriteMapper.documentFavoriteToResponse(saved);
    }

    @PreAuthorize("hasAuthority('GET_DOCUMENT_FAVORITE')")
    public List<FavoriteResponse> getDocumentFavoritesByUser() {
        Long userId = getUserIdByToken.get();
        List<Favorite> favorites = favoriteDocumentRepository.findByUserIdAndDocumentFit(userId,
                ContentStatus.PUBLISHED);
        return favorites.stream().map(favoriteMapper::documentFavoriteToResponse).toList();
    }

    @PreAuthorize("hasAuthority('REMOVE_DOCUMENT_FAVORITE')")
    public void removeDocumentFavorite(Long documentId) {
        Long userId = getUserIdByToken.get();
        Favorite favorite = favoriteDocumentRepository.findByUserIdAndDocument_Id(userId, documentId)
                .orElseThrow(() -> AppException.builder().appError(AppError.REMOVE_FROM_FAVORITE_FAILED).build());
        favoriteDocumentRepository.deleteById(favorite.getId());

    }

    @PreAuthorize("hasAuthority('CHECK_DOCUMENT_FAVORITE')")
    public boolean checkDocumentFavorite(Long documentId) {
        Long userId = getUserIdByToken.get();
        return favoriteDocumentRepository.existsByUserIdAndDocument_Id(userId, documentId);
    }
}
