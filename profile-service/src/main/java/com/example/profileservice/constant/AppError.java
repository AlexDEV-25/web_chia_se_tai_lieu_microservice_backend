package com.example.profileservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum AppError {
    UPDATE_PROFILE_FAILED(1005, "Cập nhật thông tin cá nhân thất bại", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1011, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1015, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS(1017, "Email đã tồn tại", HttpStatus.CONFLICT),
    ALREADY_FRIEND(1042, "Hai người đã là bạn bè", HttpStatus.CONFLICT),
    CANNOT_FOLLOW_YOURSELF(1043, "Không thể theo dõi chính mình", HttpStatus.BAD_REQUEST),
    INVALID_JSON_FORMAT(1046, "Dữ liệu JSON không hợp lệ", HttpStatus.BAD_REQUEST);
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}