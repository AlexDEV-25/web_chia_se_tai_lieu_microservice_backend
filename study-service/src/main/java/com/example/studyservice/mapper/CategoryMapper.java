package com.example.studyservice.mapper;

import com.example.response.CategoryResponse;
import com.example.studyservice.dto.request.CategoryRequest;
import com.example.studyservice.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse entityToResponse(Category entity);

    @Mapping(target = "id", ignore = true)
    void updateCategory(@MappingTarget Category entity, CategoryRequest request);

}
