package com.example.interactionservice.mapper;


import com.example.interactionservice.dto.response.RatingUserResponse;
import com.example.interactionservice.model.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    RatingUserResponse documentRatingToRatingResponse(Rating entity);
}
