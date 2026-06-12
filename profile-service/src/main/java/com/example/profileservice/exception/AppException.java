package com.example.profileservice.exception;


import com.example.profileservice.constant.AppError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private AppError appError;

}
