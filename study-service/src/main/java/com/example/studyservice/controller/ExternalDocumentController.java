package com.example.studyservice.controller;


import com.example.AppError;
import com.example.commondto.response.APIResponse;
import com.example.commonexception.exception.AppException;
import com.example.studyservice.dto.request.DocumentRequest;
import com.example.studyservice.dto.response.*;
import com.example.studyservice.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/external/documents")
@AllArgsConstructor
@Slf4j
public class ExternalDocumentController {
    private final DocumentService documentService;

    @GetMapping("/stats")
    public APIResponse<DocumentStatsResponse> getStats() {
        APIResponse<DocumentStatsResponse> apiResponse = new APIResponse<DocumentStatsResponse>();
        apiResponse.setResult(documentService.getStats());
        return apiResponse;
    }

    @GetMapping("/search")
    public APIResponse<DocumentResponse> search(@RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Long categoryId) {
        APIResponse<DocumentResponse> apiResponse = new APIResponse<DocumentResponse>();
        apiResponse.setResultList(documentService.search(keyword, categoryId));
        return apiResponse;
    }

    @GetMapping("/{id}")
    public APIResponse<DocumentDetailResponse> getByIdPublicDocument(@PathVariable Long id) {
        APIResponse<DocumentDetailResponse> apiResponse = new APIResponse<DocumentDetailResponse>();
        apiResponse.setResult(documentService.findByIdPublicDocument(id));
        return apiResponse;
    }

    @GetMapping
    public APIResponse<DocumentResponse> getAllPublicDocuments() {
        APIResponse<DocumentResponse> apiResponse = new APIResponse<DocumentResponse>();
        apiResponse.setResultList(documentService.getAllPublicDocuments());
        return apiResponse;
    }

    @GetMapping("/user")
    public APIResponse<DocumentResponse> getByUser(@RequestParam Long documentId, @RequestParam Long userId) {
        APIResponse<DocumentResponse> apiResponse = new APIResponse<DocumentResponse>();
        apiResponse.setResultList(documentService.getDocumentsByUser(userId, documentId));
        return apiResponse;
    }

    @GetMapping("/category")
    public APIResponse<DocumentResponse> getByCategory(@RequestParam Long categoryId, @RequestParam Long documentId) {
        APIResponse<DocumentResponse> apiResponse = new APIResponse<DocumentResponse>();
        apiResponse.setResultList(documentService.getDocumentsByCategory(categoryId, documentId));
        return apiResponse;
    }

    @GetMapping("/user/{userId}")
    public APIResponse<DocumentResponse> getAllDocumentsByUser(@PathVariable Long userId) {
        APIResponse<DocumentResponse> apiResponse = new APIResponse<DocumentResponse>();
        apiResponse.setResultList(documentService.getAllDocumentsByUser(userId));
        return apiResponse;
    }

    @GetMapping("/count/{userId}")
    public APIResponse<Long> countDocumentOfUser(@PathVariable Long userId) {
        APIResponse<Long> apiResponse = new APIResponse<Long>();
        apiResponse.setResult(documentService.countDocumentOfUser(userId));
        return apiResponse;
    }

    @PutMapping("/view/{id}")
    public APIResponse<Void> increaseView(@PathVariable Long id) {
        documentService.increaseView(id);

        return APIResponse.<Void>builder().build();
    }

    @PostMapping("/download/{id}")
    public APIResponse<Void> increaseDownload(@PathVariable Long id) {
        documentService.increaseDownload(id);
        return APIResponse.<Void>builder().build();
    }

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<DocumentDetailResponse> create(@RequestPart("file") MultipartFile file,
                                                      @RequestPart("data") String dataJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DocumentRequest dto = mapper.readValue(dataJson, DocumentRequest.class);
            APIResponse<DocumentDetailResponse> apiResponse = new APIResponse<DocumentDetailResponse>();
            apiResponse.setResult(documentService.uploadFile(file, dto));
            return apiResponse;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw AppException.builder().appError(AppError.INVALID_JSON_FORMAT).build();
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws Exception {

        FileResponse file = documentService.downloadById(id);

        return ResponseEntity.ok().contentLength(file.getLength()).contentType(file.getMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getResource());
    }

    @GetMapping("/my-document")
    public APIResponse<DocumentUserResponse> getMyDocument() {
        APIResponse<DocumentUserResponse> apiResponse = new APIResponse<DocumentUserResponse>();
        apiResponse.setResultList(documentService.getMyDocument());
        return apiResponse;
    }

    @GetMapping("/my-document/{id}")
    public APIResponse<DocumentDetailResponse> getMyDocumentDetail(@PathVariable Long id) {
        APIResponse<DocumentDetailResponse> apiResponse = new APIResponse<DocumentDetailResponse>();
        apiResponse.setResult(documentService.getMyDocumentDetail(id));
        return apiResponse;
    }

    @PutMapping("/my-document/{id}")
    public APIResponse<DocumentUserResponse> updateMyDocument(@PathVariable Long id,
                                                              @RequestBody @Valid DocumentRequest dto) {
        APIResponse<DocumentUserResponse> apiResponse = new APIResponse<DocumentUserResponse>();
        apiResponse.setResult(documentService.updateMyDocument(id, dto));
        return apiResponse;
    }

    @DeleteMapping("/my-document/{id}")
    public APIResponse<Void> deleteMyDocument(@PathVariable Long id) {
        documentService.deleteMyDocument(id);
        return APIResponse.<Void>builder().build();
    }

    @GetMapping("/my-document/count")
    public APIResponse<Long> countMyDocument() {
        APIResponse<Long> apiResponse = new APIResponse<Long>();
        apiResponse.setResult(documentService.countMyDocument());
        return apiResponse;
    }

    @GetMapping("/admin/{id}")
    public APIResponse<DocumentDetailResponse> getById(@PathVariable Long id) {
        APIResponse<DocumentDetailResponse> apiResponse = new APIResponse<DocumentDetailResponse>();
        apiResponse.setResult(documentService.findById(id));
        return apiResponse;
    }

    @GetMapping("/admin")
    public APIResponse<DocumentAdminResponse> getAll() {
        APIResponse<DocumentAdminResponse> apiResponse = new APIResponse<DocumentAdminResponse>();
        apiResponse.setResultList(documentService.findAll());
        return apiResponse;
    }

    @DeleteMapping("/admin/{id}")
    public APIResponse<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return new APIResponse<Void>();
    }

    @PutMapping("/admin/{id}")
    public APIResponse<DocumentDetailResponse> update(@PathVariable Long id, @RequestBody DocumentRequest dto) {
        APIResponse<DocumentDetailResponse> apiResponse = new APIResponse<DocumentDetailResponse>();
        apiResponse.setResult(documentService.update(id, dto));
        return apiResponse;
    }

}
