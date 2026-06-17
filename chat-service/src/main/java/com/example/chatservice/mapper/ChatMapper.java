package com.example.chatservice.mapper;

import com.example.chatservice.dto.response.DocumentSearchAIResponse;
import com.example.chatservice.dto.response.LectureResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    LectureResponse documentSearchAIResponseToLectureResponse(DocumentSearchAIResponse response);
}
