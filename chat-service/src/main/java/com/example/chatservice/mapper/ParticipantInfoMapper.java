package com.example.chatservice.mapper;

import com.example.chatservice.dto.response.ParticipantInfoResponse;
import com.example.chatservice.model.ParticipantInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantInfoMapper {
    
    @Mapping(target = "userStatus", source = "user.status")
    ParticipantInfoResponse entityToResponse(ParticipantInfo entity);

}
