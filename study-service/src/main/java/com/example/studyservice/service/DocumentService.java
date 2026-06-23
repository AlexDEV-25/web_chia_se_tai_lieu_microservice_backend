package com.example.studyservice.service;

import com.example.AppError;
import com.example.ContentStatus;
import com.example.commondto.response.DocumentInfoResponse;
import com.example.commondto.response.DocumentSearchAIResponse;
import com.example.commonexception.exception.AppException;
import com.example.commonsecurity.helper.GetUserIdByToken;
import com.example.constant.NotificationType;
import com.example.event.SystemNotificationEvent;
import com.example.studyservice.dto.request.DocumentRequest;
import com.example.studyservice.dto.response.*;
import com.example.studyservice.mapper.DocumentMapper;
import com.example.studyservice.model.Category;
import com.example.studyservice.model.Document;
import com.example.studyservice.repository.CategoryRepository;
import com.example.studyservice.repository.DocumentRepository;
import com.example.studyservice.repository.httpclient.FileClient;
import com.example.studyservice.repository.httpclient.ProfileClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;
    private final DocumentMapper documentMapper;
    private final FileClient fileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProfileClient profileClient;

    @Value("${app.domain.frontend}")
    private String frontendDomain;

    @PreAuthorize("hasRole('ADMIN')")
    public DocumentDetailResponse findById(Long id) {
        Document find = documentRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        return documentMapper.documentToDocumentDetailResponse(find);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<DocumentAdminResponse> findAll() {
        List<Document> documents = documentRepository.findAll();
        return documents.stream().map(documentMapper::documentToDocumentAdminResponse)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Long adminId = GetUserIdByToken.get();
        Document entity = documentRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        DocumentEventDTO dto = documentMapper.documentToDocumentDTO(entity);

        deleteFile(entity);
        deleteByKey(id);

        adminDeleteEvent(dto, adminId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DocumentDetailResponse update(Long id, DocumentRequest request) {
        Long adminId = GetUserIdByToken.get();
        Document entity = documentRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());

        ContentStatus initialStatus = entity.getStatus();

        documentMapper.updateDocument(entity, request);
        entity.setUpdatedAt(LocalDateTime.now());
        Document saved = documentRepository.save(entity);

        adminUpdateEvent(saved, initialStatus, adminId);

        return documentMapper.documentToDocumentDetailResponse(saved);
    }

    @PreAuthorize("hasAuthority('GET_MY_DOCUMENT')")
    public List<DocumentUserResponse> getMyDocument() {
        Long userId = GetUserIdByToken.get();
        List<Document> documents = documentRepository.findByUserId(userId);
        return documents.stream().map(documentMapper::documentToDocumentUserResponse).toList();
    }

    @PreAuthorize("hasAuthority('GET_MY_DOCUMENT_DETAIL')")
    public DocumentDetailResponse getMyDocumentDetail(Long id) {
        Long userId = GetUserIdByToken.get();
        Document entity = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        return documentMapper.documentToDocumentDetailResponse(entity);
    }

    @PreAuthorize("hasAuthority('UPDATE_MY_DOCUMENT')")
    public DocumentUserResponse updateMyDocument(Long id, DocumentRequest request) {
        Long userId = GetUserIdByToken.get();
        Document entity = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        boolean initialState = entity.isHide();

        documentMapper.updateDocument(entity, request);
        entity.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(entity);

        if (!initialState && saved.isHide() && saved.getStatus() == ContentStatus.PUBLISHED) {

            buildEvent(saved.getUserId(), saved.getAuthorName(),
                    null, null,
                    "Tác giả " + saved.getAuthorName() + " đã ẩn" + "Tài liệu: \"" + saved.getTitle() + "\"",
                    null, NotificationType.INFO,
                    "author-hide-document-to-follower");
        }


        return documentMapper.documentToDocumentUserResponse(saved);
    }

    @PreAuthorize("hasAuthority('DELETE_MY_DOCUMENT')")
    public void deleteMyDocument(Long id) {
        Long userId = GetUserIdByToken.get();
        Document entity = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        DocumentEventDTO dto = documentMapper.documentToDocumentDTO(entity);

        deleteFile(entity);
        deleteByKey(id);

        if (dto.getStatus() != ContentStatus.PENDING) {
            buildEvent(dto.getUserId(), dto.getAuthorName(),
                    null, null,
                    "Tác giả " + dto.getAuthorName() + " đã xóa" + "Tài liệu: \"" + dto.getTitle() + "\"",
                    null, NotificationType.INFO,
                    "author-delete-document-to-follower");
        }
    }

    @PreAuthorize("hasAuthority('COUNT_MY_DOCUMENT')")
    public Long countMyDocument() {
        Long userId = GetUserIdByToken.get();
        return documentRepository.countByUserId(userId);
    }

    @PreAuthorize("hasAuthority('UPLOAD_FILE')")
    public DocumentDetailResponse uploadFile(MultipartFile fileToSave, String dataJson) {
        Long userId = GetUserIdByToken.get();
        try {
            ObjectMapper mapper = new ObjectMapper();
            DocumentRequest dto = mapper.readValue(dataJson, DocumentRequest.class);

            Document document = documentMapper.requestToDocument(dto);
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());
            document.setViewsCount(0L);
            document.setDownloadsCount(0L);

            System.out.println("til" + document.getTitle());
            try {
                document.setAuthorName(profileClient.getUserDetail(userId).getResult().getFullName());
                Map<String, Object> handleDoc = fileClient.uploadPdf(fileToSave).getResult();
                String url = (String) handleDoc.get("secure_url");
                String publicId = (String) handleDoc.get("public_id");
                document.setFileUrl(url);
                String thumbnailUrl = fileClient.getThumbnail(publicId).getResult();
                document.setThumbnailUrl(thumbnailUrl);
                System.out.println("thum" + document.getThumbnailUrl());
            } catch (Exception e) {
                log.warn(e.getMessage());
            }

            Category category = dto.getCategoryId() != null ?
                    categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> AppException.builder().appError(AppError.CATEGORY_NOT_FOUND).build())
                    : null;
            document.setCategory(category);
            Document saved = documentRepository.save(document);
            return documentMapper.documentToDocumentDetailResponse(saved);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw AppException.builder().appError(AppError.INVALID_JSON_FORMAT).build();
        }
    }

    @PreAuthorize("hasAuthority('DOWNLOAD_FILE')")
    public FileResponse downloadById(Long id) throws Exception {
        Document doc = documentRepository.findByIdAndStatusAndHideFalse(id, ContentStatus.PUBLISHED)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());

        return fileClient.downloadFile(doc.getFileUrl()).getResult();

    }

    public List<DocumentResponse> search(String keyword, Long categoryId) {

        Long userId = GetUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.searchWithoutLogin(keyword, categoryId, ContentStatus.PUBLISHED);
        }
        return documentRepository.searchWhenLogin(keyword, categoryId, userId, ContentStatus.PUBLISHED);
    }

    public List<DocumentResponse> getAllPublicDocuments() {
        Long userId = GetUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.getAllWithoutLogin(ContentStatus.PUBLISHED);
        }
        return documentRepository.getAllWhenLogin(userId, ContentStatus.PUBLISHED);

    }

    public List<DocumentSearchAIResponse> getAllPublicDocumentsForAI() {
        return documentRepository.findByStatusAndHideFalse(ContentStatus.PUBLISHED)
                .stream().map(documentMapper::documentToDocumentSearchAIResponse).toList();
    }

    public DocumentInfoResponse getAllPublicDocumentsForInteraction(Long documentId) {
        Document find = documentRepository.findByIdAndStatusAndHideFalse(documentId, ContentStatus.PUBLISHED)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        return documentMapper.documentToDocumentInfoResponse(find);
    }

    public List<DocumentResponse> getDocumentsByUser(Long authorId, Long currentDocumentId) {
        Long userId = GetUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.getByUserWithoutLoginAndDifferentCurrentDocument(authorId, currentDocumentId,
                    ContentStatus.PUBLISHED);
        }
        return documentRepository.getByUserWhenLoginAndDifferentCurrentDocument(authorId, userId,
                currentDocumentId, ContentStatus.PUBLISHED);

    }

    public List<DocumentResponse> getDocumentsByCategory(Long categoryId, Long currentDocumentId) {
        Long userId = GetUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.getByCategoryWithoutLoginAndDifferentCurrentDocument(categoryId,
                    currentDocumentId, ContentStatus.PUBLISHED);
        }
        return documentRepository.getByCategoryWhenLoginAndDifferentCurrentDocument(categoryId, userId,
                currentDocumentId, ContentStatus.PUBLISHED);

    }

    public List<DocumentResponse> getAllDocumentsByUser(Long authorId) {
        Long userId = GetUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.getByUserWithoutLogin(authorId, ContentStatus.PUBLISHED);
        }
        return documentRepository.getByUserWhenLogin(authorId, userId, ContentStatus.PUBLISHED);
    }

    public DocumentStatsResponse getStats() {
        return documentRepository.getStats(ContentStatus.PUBLISHED);
    }

    public DocumentDetailResponse findByIdPublicDocument(Long id) {
        Document find = documentRepository.findByIdAndStatusAndHideFalse(id, ContentStatus.PUBLISHED)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        return documentMapper.documentToDocumentDetailResponse(find);
    }

    public void increaseView(Long id) {
        documentRepository.findById(id).ifPresent(entity -> {
            entity.setViewsCount(entity.getViewsCount() + 1);
            documentRepository.save(entity);
        });
    }

    @PreAuthorize("hasAuthority('INCREASE_DOWNLOAD')")
    public void increaseDownload(Long id) {
        documentRepository.findById(id).ifPresent(entity -> {
            entity.setDownloadsCount(entity.getDownloadsCount() + 1);
            documentRepository.save(entity);
        });
    }

    public Long countDocumentOfUser(Long userId) {
        return documentRepository.countByUserIdAndStatusAndHideFalse(userId, ContentStatus.PUBLISHED);
    }

    private void deleteByKey(Long id) {
        // xóa nhưng thằng liên quan
        documentRepository.deleteById(id);
    }

    private void deleteFile(Document entity) {
        fileClient.deleteFile(entity.getFileUrl());
        fileClient.deleteFile(entity.getThumbnailUrl());
    }

    private void adminUpdateEvent(Document saved, ContentStatus initialStatus, Long adminId) {
        if (initialStatus == ContentStatus.PENDING && saved.getStatus() == ContentStatus.PUBLISHED) {
            buildEvent(adminId, "ADMIN",
                    saved.getUserId(), saved.getAuthorName(),
                    "Tài Liệu \" " + saved.getTitle() + "\" đã được duyệt",
                    frontendDomain + "/document/" + saved.getId(), NotificationType.INFO,
                    "admin-approve-document-to-author");
            if (!saved.isHide()) {
                buildEvent(saved.getUserId(), saved.getAuthorName(),
                        null, null,
                        "người dùng \" " + saved.getAuthorName() + "\" đã đăng tài liệu mới",
                        frontendDomain + "/document/" + saved.getId(), NotificationType.INFO,
                        "admin-approve-document-to-follower");
            }
        }
        if (initialStatus == ContentStatus.PUBLISHED && saved.getStatus() == ContentStatus.HIDDEN) {
            buildEvent(adminId, "ADMIN",
                    saved.getUserId(), saved.getAuthorName(),
                    "Tài Liệu \" " + saved.getTitle() + "\" của bạn tạm thời bị ẩn",
                    null, NotificationType.WARNING,
                    "admin-hide-document-to-author");

            if (!saved.isHide()) {
                buildEvent(saved.getUserId(), saved.getAuthorName(),
                        null, null,
                        "Tài liệu: \" " + saved.getTitle() + "\" của tác giả"
                                + saved.getAuthorName() + " tạm thời bị ẩn",
                        null, NotificationType.INFO,
                        "admin-hide-document-to-follower");
            }
        }
    }

    private void adminDeleteEvent(DocumentEventDTO dto, Long adminId) {
        buildEvent(adminId, "ADMIN",
                dto.getUserId(), dto.getAuthorName(),
                "Tài Liệu \" " + dto.getTitle() + "\" của bạn đã bị xóa",
                null, NotificationType.WARNING,
                "admin-delete-document-to-author");

        if (dto.getStatus() != ContentStatus.PENDING) {
            buildEvent(dto.getUserId(), dto.getAuthorName(),
                    null, null,
                    "Tài liệu: \" " + dto.getTitle() + "\" của tác giả" + dto.getAuthorName() + "  đã bị xóa",
                    null, NotificationType.INFO,
                    "admin-delete-document-to-follower");
        }
    }

    private void buildEvent(Long senderId, String senderName, Long receiverId, String receiverName, String content, String link, NotificationType type, String topic) {
        SystemNotificationEvent systemNotificationEvent = SystemNotificationEvent.builder()
                .channel("SYSTEM")
                .senderId(senderId).senderName(senderName)
                .receiverId(receiverId).receiverName(receiverName)
                .content(content)
                .link(link).type(type)
                .build();
        kafkaTemplate.send(topic, systemNotificationEvent);
    }
}
