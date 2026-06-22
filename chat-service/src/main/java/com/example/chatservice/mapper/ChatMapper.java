package com.example.chatservice.mapper;

import com.example.chatservice.dto.response.LectureResponse;
import com.example.commondto.response.DocumentSearchAIResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "average", ignore = true)
    @Mapping(target = "total", ignore = true)
    LectureResponse documentSearchAIResponseToLectureResponse(DocumentSearchAIResponse response);
}
