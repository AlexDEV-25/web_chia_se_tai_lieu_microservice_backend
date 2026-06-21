package com.example.chatservice.mapper;

import com.example.chatservice.dto.response.ParticipantInfoResponse;
import com.example.chatservice.model.ParticipantInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantInfoMapper {
    
    ParticipantInfoResponse entityToResponse(ParticipantInfo entity);

}
