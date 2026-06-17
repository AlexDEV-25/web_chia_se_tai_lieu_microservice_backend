package com.example.chatservice.mapper;

import com.example.app.dto.response.participantinfo.ParticipantInfoResponse;
import com.example.app.model.ParticipantInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantInfoMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userStatus", source = "user.status")
    ParticipantInfoResponse entityToResponse(ParticipantInfo entity);

}
