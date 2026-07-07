package com.example.interactionservice.dto.response;

public interface RatingAdminProjection {

    Long getId();

    String getDocumentTitle();

    Double getAverage();

    Long getTotal();
}