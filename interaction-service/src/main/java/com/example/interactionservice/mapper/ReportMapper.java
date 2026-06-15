package com.example.interactionservice.mapper;

import com.example.interactionservice.dto.request.ReportRequest;
import com.example.interactionservice.dto.response.ReportDetailAdminResponse;
import com.example.interactionservice.dto.response.ReportUserResponse;
import com.example.interactionservice.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportDetailAdminResponse documentReportToResponse(Report entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Report reportToDocumentReport(ReportRequest request);

    ReportUserResponse documentReportToReportUserResponse(Report entity);

}
