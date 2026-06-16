package com.example.interactionservice.service;


import com.example.constant.NotificationType;
import com.example.event.SystemNotificationEvent;
import com.example.interactionservice.constant.AppError;
import com.example.interactionservice.dto.request.CommentRequest;
import com.example.interactionservice.dto.request.DisplayRequest;
import com.example.interactionservice.dto.response.CommentResponse;
import com.example.interactionservice.dto.response.CommentTreeResponse;
import com.example.interactionservice.exception.AppException;
import com.example.interactionservice.helper.GetUserIdByToken;
import com.example.interactionservice.mapper.CommentMapper;
import com.example.interactionservice.model.Comment;
import com.example.interactionservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CommentService {

    private final CommentRepository documentRepo;
    private final CommentMapper mapper;
    private final GetUserIdByToken getUserIdByToken;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.domain.frontend}")
    private String frontendDomain;

    @PreAuthorize("hasAuthority('POST_COMMENT')")
    public CommentResponse saveMyComment(CommentRequest req) {

        Long userId = getUserIdByToken.get();

        Comment parent = getParentDocument(req.getParentId());

        Comment comment = Comment.builder().content(req.getContent()).userId(userId)
                .parent(parent).level(calcLevel(parent)).createdAt(LocalDateTime.now()).hide(false).build();

        Comment saved = documentRepo.save(comment);

        if (parent != null) {
            SystemNotificationEvent systemNotificationEvent = SystemNotificationEvent.builder()
                    .channel("SYSTEM")
                    .senderId(saved.getParent().getUserId())
                    .senderName(saved.getParent().getUserName())
                    .receiverId(saved.getUserId())
                    .receiverName(saved.getUserName())
                    .content("người dùng " + saved.getUserName() + " đã trả lời bình luận của bạn")
                    .link(frontendDomain + "/document/" + saved.getDocumentId())
                    .type(NotificationType.INFO)
                    .build();
            kafkaTemplate.send("reply-comment", systemNotificationEvent);
        }


        return mapper.documentCommentToCommentResponse(saved);
    }

    @PreAuthorize("hasAuthority('UPDATE_MY_COMMENT')")
    public CommentResponse updateMyComment(Long id, CommentRequest req) {
        Long userId = getUserIdByToken.get();
        Comment c = documentRepo.findByIdAndUserIdAndHideFalse(id, userId)
                .orElseThrow(() -> new AppException(AppError.CANNOT_UPDATE_COMMENT));
        c.setContent(req.getContent());
        c.setUpdatedAt(LocalDateTime.now());
        return mapper.documentCommentToCommentResponse(documentRepo.save(c));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CommentResponse hide(Long id, DisplayRequest req) {
        Comment c = documentRepo.findById(id).orElseThrow(() -> new RuntimeException("Không thấy comment"));
        c.setHide(req.isHide());
        c.setUpdatedAt(LocalDateTime.now());
        return mapper.documentCommentToCommentResponse(c);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CommentResponse> getAllDocumentComments() {
        return documentRepo.findAll().stream().map(mapper::documentCommentToCommentResponse).toList();
    }

    public List<CommentTreeResponse> getDocumentTree(Long docId) {
        List<Comment> list = documentRepo.findByDocumentIdAndHideFalseOrderByLevelAscCreatedAtAsc(docId);
        return buildTreeDocument(list);
    }

    private List<CommentTreeResponse> buildTreeDocument(List<Comment> list) {

        Map<Long, CommentTreeResponse> map = new HashMap<>();

        for (Comment c : list) {
            CommentTreeResponse dto = mapper.documentCommentToCommentTreeResponse(c);
            map.put(dto.getId(), dto);
        }

        return buildTree(map);
    }

    private List<CommentTreeResponse> buildTree(Map<Long, CommentTreeResponse> map) {

        List<CommentTreeResponse> roots = new ArrayList<>();

        for (CommentTreeResponse dto : map.values()) {

            if (dto.getParentId() == null) {
                roots.add(dto);
            } else {
                CommentTreeResponse parent = map.get(dto.getParentId());
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