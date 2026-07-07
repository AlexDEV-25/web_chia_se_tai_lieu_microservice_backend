package com.example.studyservice.dto.response;

public interface DocumentResponse {
    Long getId();

    String getTitle();

    String getDescription();

    String getThumbnailUrl();

    String getAuthorName();

    Long getViewsCount();

    Long getDownloadsCount();

    Boolean getFavorite();
}