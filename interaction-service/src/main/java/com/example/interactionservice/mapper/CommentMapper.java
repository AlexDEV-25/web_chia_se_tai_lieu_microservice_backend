package com.example.interactionservice.mapper;


import com.example.interactionservice.dto.request.CommentRequest;
import com.example.interactionservice.dto.response.CommentResponse;
import com.example.interactionservice.dto.response.CommentTreeResponse;
import com.example.interactionservice.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(source = "parent.id", target = "parentId")
    CommentResponse documentCommentToCommentResponse(Comment entity);
	
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(target = "children", ignore = true)
    CommentTreeResponse documentCommentToCommentTreeResponse(Comment entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "hide", ignore = true)
    void updateDocumentComment(@MappingTarget Comment entity, CommentRequest request);


}
