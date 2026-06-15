package com.example.studyservice.mapper;

import com.example.studyservice.dto.response.FavoriteResponse;
import com.example.studyservice.model.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(source = "document.id", target = "contentId")
    @Mapping(source = "document.title", target = "title")
    @Mapping(source = "document.thumbnailUrl", target = "thumbnailUrl")
    FavoriteResponse documentFavoriteToResponse(Favorite entity);

}
