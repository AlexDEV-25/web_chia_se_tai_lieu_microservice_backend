package com.example.interactionservice.dto.response;

public interface CommentTotalAdminProjection {

    Long getDocumentId();

    String getDocumentTitle();

    Long getCommentTotal();
}