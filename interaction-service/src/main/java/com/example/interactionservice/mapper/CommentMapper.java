package com.example.interactionservice.mapper;


import com.example.commondto.response.CommentDetailAdminResponse;
import com.example.interactionservice.dto.response.CommentTreeUserResponse;
import com.example.interactionservice.dto.response.CommentUserResponse;
import com.example.interactionservice.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "parent.id", target = "parentId")
    CommentUserResponse documentCommentToCommentResponse(Comment entity);

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(target = "children", ignore = true)
    CommentTreeUserResponse documentCommentToCommentTreeResponse(Comment entity);

    CommentDetailAdminResponse documentCommentToCommentDetailAdminResponse(Comment entity);

}
