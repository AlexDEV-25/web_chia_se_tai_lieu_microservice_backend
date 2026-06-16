package com.example.fileservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum AppError {


    INVALID_IMAGE_FORMAT(1006, "Ảnh không đúng định dạng", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    INVALID_DOCUMENT_FORMAT(1009, "Tài liệu không đúng định dạng", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    INVALID_TOKEN(1011, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1036, "Bạn không có quyền thực hiện chức năng này", HttpStatus.FORBIDDEN);


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}