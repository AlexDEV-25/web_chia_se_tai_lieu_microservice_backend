package com.example.interactionservice.mapper;

import com.example.interactionservice.dto.response.ReportDetailAdminResponse;
import com.example.interactionservice.dto.response.ReportUserResponse;
import com.example.interactionservice.model.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportDetailAdminResponse documentReportToResponse(Report entity);

    ReportUserResponse documentReportToReportUserResponse(Report entity);

}
