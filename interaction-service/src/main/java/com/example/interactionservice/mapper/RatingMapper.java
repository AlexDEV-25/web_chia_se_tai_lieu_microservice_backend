package com.example.interactionservice.mapper;


import com.example.interactionservice.dto.request.RatingRequest;
import com.example.interactionservice.dto.response.RatingUserResponse;
import com.example.interactionservice.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Rating requestToDocumentRating(RatingRequest request);

    RatingUserResponse documentRatingToRatingResponse(Rating entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateDocumentRating(@MappingTarget Rating entity, RatingRequest request);

}
