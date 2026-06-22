package com.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum AppError {

    TOKEN_EXPIRED(1010, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1011, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    INVALID_SECRET_KEY_LENGTH(1012, "Độ dài secret key không hợp lệ", HttpStatus.INTERNAL_SERVER_ERROR),
    JOSE_PROCESSING_ERROR(1013, "Lỗi xử lý JWT/JOSE", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(1015, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_ACTIVATED(1016, "Tài khoản chưa được kích hoạt", HttpStatus.FORBIDDEN),
    EMAIL_ALREADY_EXISTS(1017, "Email đã tồn tại", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS(1018, "Username đã tồn tại", HttpStatus.CONFLICT),
    ACCOUNT_ALREADY_ACTIVATED(1019, "Tài khoản đã được kích hoạt", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_CODE(1020, "Mã xác thực không đúng", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(1021, "Email không tồn tại", HttpStatus.NOT_FOUND),
    GOOGLE_LOGIN_FAILED(1022, "Đăng nhập bằng Google thất bại", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1036, "Bạn không có quyền thực hiện chức năng này", HttpStatus.FORBIDDEN),
    LOGIN_FAILED(1044, "Đăng nhập thất bại", HttpStatus.UNAUTHORIZED),
    FAILED_TO_PARSE_DATA(1045, "Không thể phân tích dữ liệu", HttpStatus.BAD_REQUEST),
    INCORRECT_PASSWORD(1047, "Mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    CREATE_PROFILE_FAILED(1048, "đăng ký thất bại", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}