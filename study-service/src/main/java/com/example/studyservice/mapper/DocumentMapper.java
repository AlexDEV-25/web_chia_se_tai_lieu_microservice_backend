package com.example.studyservice.mapper;

import com.example.response.DocumentInfoResponse;
import com.example.response.DocumentSearchAIResponse;
import com.example.studyservice.dto.request.DocumentRequest;
import com.example.studyservice.dto.response.DocumentAdminResponse;
import com.example.studyservice.dto.response.DocumentDetailResponse;
import com.example.studyservice.dto.response.DocumentEventDTO;
import com.example.studyservice.dto.response.DocumentUserResponse;
import com.example.studyservice.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "fileUrl", ignore = true)
    @Mapping(target = "downloadsCount", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    Document requestToDocument(DocumentRequest request);


    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    DocumentDetailResponse documentToDocumentDetailResponse(Document entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "fileUrl", ignore = true)
    @Mapping(target = "downloadsCount", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateDocument(@MappingTarget Document document, DocumentRequest request);

    DocumentUserResponse documentToDocumentUserResponse(Document entity);

    @Mapping(source = "category.name", target = "categoryName")
    DocumentAdminResponse documentToDocumentAdminResponse(Document entity);

    @Mapping(source = "category.name", target = "categoryName")
    DocumentSearchAIResponse documentToDocumentSearchAIResponse(Document entity);

    DocumentInfoResponse documentToDocumentInfoResponse(Document entity);

    DocumentEventDTO documentToDocumentDTO(Document entity);

}
