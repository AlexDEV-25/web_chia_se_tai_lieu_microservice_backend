package com.example.fileservice.exception;


import com.example.AppError;
import com.example.commondto.response.APIResponse;
import com.example.commonexception.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<APIResponse<String>> handlingRuntimeException(RuntimeException e) {
        APIResponse<String> error = new APIResponse<String>();
        error.setCode(9999);
        error.setMessage("lỗi khi chạy");
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<String>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        APIResponse<String> error = new APIResponse<String>();
        String message = e.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        error.setCode(9999);
        error.setMessage(message);
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<APIResponse<Void>> handlingAccessDeniedException(AccessDeniedException exception) {
        AppError errorCode = AppError.ACCESS_DENIED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(APIResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<String>> handleJsonException(Exception ex, HttpServletRequest request) {
        APIResponse<String> error = new APIResponse<>();
        if (request.getRequestURI().contains("/video")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        error.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<APIResponse<AppException>> handlingAppException(AppException e) {
        APIResponse<AppException> error = new APIResponse<>();
        error.setCode(e.getAppError().getCode());
        error.setMessage(e.getAppError().getMessage());
        return ResponseEntity.status(e.getAppError().getStatusCode()).body(error);
    }

}
