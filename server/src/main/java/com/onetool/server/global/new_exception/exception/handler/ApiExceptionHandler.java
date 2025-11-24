package com.onetool.server.global.new_exception.exception.handler;

import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.ErrorCodeIfs;
import com.onetool.server.global.new_exception.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Object> apiException(
            ApiException apiException
    ) {
        ErrorCodeIfs errorCode = apiException.getErrorCode();

        log.error("[Custom Error] {} : {}", errorCode.getHttpStatus(), apiException.getCustomErrorMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        ErrorResponse.generateErrorResponse(apiException)
                );
    }
}
