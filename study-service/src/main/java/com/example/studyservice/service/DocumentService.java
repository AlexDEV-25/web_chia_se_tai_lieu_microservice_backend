package com.example.studyservice.service;


import com.example.constant.NotificationType;
import com.example.event.SystemNotificationEvent;
import com.example.studyservice.constant.AppError;
import com.example.studyservice.constant.ContentStatus;
import com.example.studyservice.dto.request.DocumentRequest;
import com.example.studyservice.dto.response.*;
import com.example.studyservice.exception.AppException;
import com.example.studyservice.helper.GetUserIdByToken;
import com.example.studyservice.mapper.DocumentMapper;
import com.example.studyservice.model.Category;
import com.example.studyservice.model.Document;
import com.example.studyservice.repository.CategoryRepository;
import com.example.studyservice.repository.DocumentRepository;
import com.example.studyservice.repository.httpclient.FileClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DocumentMapper documentMapper;
    private final GetUserIdByToken getUserIdByToken;
    private final FileClient fileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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
        Long adminId = getUserIdByToken.get();
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
        Long adminId = getUserIdByToken.get();
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
        Long userId = getUserIdByToken.get();
        List<Document> documents = documentRepository.findByUserId(userId);
        return documents.stream().map(documentMapper::documentToDocumentUserResponse).toList();
    }

    @PreAuthorize("hasAuthority('GET_MY_DOCUMENT_DETAIL')")
    public DocumentDetailResponse getMyDocumentDetail(Long id) {
        Long userId = getUserIdByToken.get();
        Document entity = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());
        return documentMapper.documentToDocumentDetailResponse(entity);
    }

    @PreAuthorize("hasAuthority('UPDATE_MY_DOCUMENT')")
    public DocumentUserResponse updateMyDocument(Long id, DocumentRequest request) {
        Long userId = getUserIdByToken.get();
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
        Long userId = getUserIdByToken.get();
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
        Long userId = getUserIdByToken.get();
        return documentRepository.countByUserId(userId);
    }

    @PreAuthorize("hasAuthority('UPLOAD_FILE')")
    @Transactional
    public DocumentDetailResponse uploadFile(MultipartFile fileToSave, DocumentRequest dto) {
        Document document = documentMapper.requestToDocument(dto);
        document.setCreatedAt(LocalDateTime.now());

        try {
            Map<String, Object> handleDoc = fileClient.uploadPdf(fileToSave);

            String url = (String) handleDoc.get("secure_url");
            String publicId = (String) handleDoc.get("public_id");
            document.setFileUrl(url);

            String thumbnailUrl = fileClient.getThumbnail(publicId);
            document.setThumbnailUrl(thumbnailUrl);
        } catch (Exception e) {
            throw AppException.builder().appError(AppError.UPLOAD_DOCUMENT_FAILED).build();
        }

        Category category = dto.getCategoryId() != null ?
                categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> AppException.builder().appError(AppError.CATEGORY_NOT_FOUND).build())
                : null;
        document.setCategory(category);
        Document saved = documentRepository.save(document);
        return documentMapper.documentToDocumentDetailResponse(saved);
    }

    @PreAuthorize("hasAuthority('DOWNLOAD_FILE')")
    public FileResponse downloadById(Long id) throws Exception {
        Document doc = documentRepository.findByIdAndStatusAndHideFalse(id, ContentStatus.PUBLISHED)
                .orElseThrow(() -> AppException.builder().appError(AppError.DOCUMENT_NOT_FOUND).build());

        return fileClient.downloadFile(doc.getFileUrl());

    }

    public List<DocumentResponse> search(String keyword, Long categoryId) {

        Long userId = getUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.searchWithoutLogin(keyword, categoryId, ContentStatus.PUBLISHED);
        }
        return documentRepository.searchWhenLogin(keyword, categoryId, userId, ContentStatus.PUBLISHED);
    }

    public List<DocumentResponse> getAllPublicDocuments() {
        Long userId = getUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.getAllWithoutLogin(ContentStatus.PUBLISHED);
        }
        return documentRepository.getAllWhenLogin(userId, ContentStatus.PUBLISHED);

    }

    public List<DocumentResponse> getDocumentsByUser(Long authorId, Long currentDocumentId) {
        Long userId = getUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.getByUserWithoutLoginAndDifferentCurrentDocument(authorId, currentDocumentId,
                    ContentStatus.PUBLISHED);
        }
        return documentRepository.getByUserWhenLoginAndDifferentCurrentDocument(authorId, userId,
                currentDocumentId, ContentStatus.PUBLISHED);

    }

    public List<DocumentResponse> getDocumentsByCategory(Long categoryId, Long currentDocumentId) {
        Long userId = getUserIdByToken.get();
        if (userId == 0L) {
            return documentRepository.getByCategoryWithoutLoginAndDifferentCurrentDocument(categoryId,
                    currentDocumentId, ContentStatus.PUBLISHED);
        }
        return documentRepository.getByCategoryWhenLoginAndDifferentCurrentDocument(categoryId, userId,
                currentDocumentId, ContentStatus.PUBLISHED);

    }

    public List<DocumentResponse> getAllDocumentsByUser(Long authorId) {
        Long userId = getUserIdByToken.get();
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
