package com.example.interactionservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum AppError {

    INVALID_TOKEN(1011, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    ALREADY_RATED(1040, "Bạn đã đánh giá rồi", HttpStatus.CONFLICT),
    ALREADY_REPORTED(1041, "Bạn đã report rồi", HttpStatus.CONFLICT),
    CANNOT_UPDATE_COMMENT(1049, "cập nhật bình luận thất bại", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}