package com.example.interactionservice.service;


import com.example.AppError;
import com.example.commondto.request.DisplayRequest;
import com.example.commondto.response.CommentAdminResponse;
import com.example.commondto.response.CommentDetailAdminResponse;
import com.example.commondto.response.DocumentInfoResponse;
import com.example.commondto.response.UserDetailInfoResponse;
import com.example.commonexception.exception.AppException;
import com.example.commonsecurity.helper.GetUserIdByToken;
import com.example.constant.NotificationType;
import com.example.event.SystemNotificationEvent;
import com.example.interactionservice.dto.request.CommentRequest;
import com.example.interactionservice.dto.response.CommentTotalAdminProjection;
import com.example.interactionservice.dto.response.CommentTreeUserResponse;
import com.example.interactionservice.dto.response.CommentUserResponse;
import com.example.interactionservice.mapper.CommentMapper;
import com.example.interactionservice.model.Comment;
import com.example.interactionservice.repository.CommentRepository;
import com.example.interactionservice.repository.httpclient.ProfileClient;
import com.example.interactionservice.repository.httpclient.StudyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository documentRepo;
    private final CommentMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final StudyClient studyClient;
    private final ProfileClient profileClient;

    @Value("${app.domain.frontend}")
    private String frontendDomain;

    @PreAuthorize("hasAuthority('POST_COMMENT')")
    public CommentUserResponse saveMyComment(CommentRequest req) {
        Long userId = GetUserIdByToken.get();

        DocumentInfoResponse doc = studyClient.getAllPublicDocumentsForInteraction(req.getDocumentId()).getResult();
        UserDetailInfoResponse user = profileClient.getUserDetail(userId).getResult();

        Comment parent = getParentDocument(req.getParentId());

        Comment comment = Comment
                .builder()
                .content(req.getContent())
                .userId(userId)
                .fullName(user.getFullName())
                .parent(parent)
                .level(calcLevel(parent))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .documentTitle(doc.getTitle())
                .hide(false).build();

        Comment saved = documentRepo.save(comment);

        if (parent != null) {
            SystemNotificationEvent systemNotificationEvent = SystemNotificationEvent.builder()
                    .channel("SYSTEM")
                    .senderId(saved.getParent().getUserId())
                    .senderName(saved.getParent().getFullName())
                    .receiverId(saved.getUserId())
                    .receiverName(saved.getFullName())
                    .content("người dùng " + saved.getFullName() + " đã trả lời bình luận của bạn")
                    .link(frontendDomain + "/document/" + saved.getDocumentId())
                    .type(NotificationType.INFO)
                    .build();
            kafkaTemplate.send("reply-comment", systemNotificationEvent);
        }


        return mapper.documentCommentToCommentResponse(saved);
    }

    @PreAuthorize("hasAuthority('UPDATE_MY_COMMENT')")
    public CommentUserResponse updateMyComment(Long id, CommentRequest req) {
        Long userId = GetUserIdByToken.get();
        Comment c = documentRepo.findByIdAndUserIdAndHideFalse(id, userId)
                .orElseThrow(() -> new AppException(AppError.CANNOT_UPDATE_COMMENT));
        c.setContent(req.getContent());
        c.setUpdatedAt(LocalDateTime.now());
        return mapper.documentCommentToCommentResponse(documentRepo.save(c));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CommentDetailAdminResponse hide(Long id, DisplayRequest req) {
        Comment c = documentRepo.findById(id).orElseThrow(() -> new RuntimeException("Không thấy comment"));
        c.setHide(req.isHide());
        c.setUpdatedAt(LocalDateTime.now());
        return mapper.documentCommentToCommentDetailAdminResponse(c);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CommentTotalAdminProjection> getTotalCommentOfDocument() {
        return documentRepo.getTotalCommentOfDocument();
    }

    public List<CommentAdminResponse> findDocumentCommentsLast7Days() {
        return documentRepo.findDocumentCommentsLast7Days(LocalDateTime.now().minusDays(7));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CommentDetailAdminResponse> getDetailDocumentComments(Long documentId) {
        return documentRepo.findByDocumentId(documentId).stream().map(mapper::documentCommentToCommentDetailAdminResponse).toList();
    }

    public List<CommentTreeUserResponse> getDocumentTree(Long docId) {
        List<Comment> list = documentRepo.findByDocumentIdAndHideFalseOrderByLevelAscCreatedAtAsc(docId);
        return buildTreeDocument(list);
    }

    private List<CommentTreeUserResponse> buildTreeDocument(List<Comment> list) {

        Map<Long, CommentTreeUserResponse> map = new HashMap<>();

        for (Comment c : list) {
            CommentTreeUserResponse dto = mapper.documentCommentToCommentTreeResponse(c);
            map.put(dto.getId(), dto);
        }

        return buildTree(map);
    }

    private List<CommentTreeUserResponse> buildTree(Map<Long, CommentTreeUserResponse> map) {

        List<CommentTreeUserResponse> roots = new ArrayList<>();

        for (CommentTreeUserResponse dto : map.values()) {

            if (dto.getParentId() == null) {
                roots.add(dto);
            } else {
                CommentTreeUserResponse parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return roots;
    }

    private Long calcLevel(Comment parent) {
        return parent == null ? 0L : parent.getLevel() + 1;
    }

    private Comment getParentDocument(Long parentId) {
        if (parentId == null)
            return null;
        return documentRepo.findById(parentId).orElseThrow(() -> new RuntimeException("Không tìm thấy parent"));
    }

}