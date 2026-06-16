package com.example.studyservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum AppError {

    UPLOAD_DOCUMENT_FAILED(1003, "Upload tài liệu thất bại", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1011, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    CATEGORY_NOT_FOUND(1023, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    DOCUMENT_NOT_FOUND(1028, "Không tìm thấy tài liệu", HttpStatus.NOT_FOUND),
    REMOVE_FROM_FAVORITE_FAILED(1033, "Xóa khỏi kho yêu thích thất bại", HttpStatus.BAD_REQUEST),
    INVALID_JSON_FORMAT(1046, "Dữ liệu JSON không hợp lệ", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}