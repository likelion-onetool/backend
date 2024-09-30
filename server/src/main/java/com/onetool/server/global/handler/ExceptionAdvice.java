package com.onetool.server.global.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.onetool.server.global.exception.*;
import com.onetool.server.global.exception.codes.ErrorCode;
import com.onetool.server.global.exception.codes.reason.Reason;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice {

    /**
     * 바인딩 에러 처리
     * @param e
     * @return
     */
    @ExceptionHandler
    public ResponseEntity<Object> validation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ApiResponse<?> baseResponse = ApiResponse.onFailure(ErrorCode.BINDING_ERROR.getCode(), message, null);
        return handleExceptionInternal(baseResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> io(MultipartException e) {
        ApiResponse<?> baseResponse = ApiResponse.onFailure(ErrorCode.BLUEPRINT_FILE_NECESSARY.getCode(), ErrorCode.BLUEPRINT_FILE_NECESSARY.getMessage(), null);
        return handleExceptionInternal(baseResponse);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ApiResponse<?> handleNotFoundMemberException(MemberNotFoundException e) {
        return ApiResponse.onFailure("404", "유저를 찾을 수 없습니다.", null);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ApiResponse<?> handleDuplicateMemberException(DuplicateMemberException e) {
        return ApiResponse.onFailure("400", "이메일이 중복됩니다.", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.toString());
        return ApiResponse.onFailure("400", "인증 중 문제가 발생했습니다.", null);
    }

    @ExceptionHandler(BlueprintNotFoundException.class)
    public ApiResponse<?> handleBlueprintNotFoundException(BlueprintNotFoundException e) {
        return ApiResponse.onFailure("404", "도면을 찾을 수 없습니다.", null);
    }

    @ExceptionHandler(InvalidSortTypeException.class)
    public ApiResponse<?> handleInvalidSortTypeException(InvalidSortTypeException e) {
        return ApiResponse.onFailure("400", "올바르지 않은 정렬 방식입니다.", null);
    }

    /**
     * 서버 에러
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> exception(RuntimeException e) {
        log.error(e.getMessage());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorCode.getCode(), e.getMessage(), null);
        return handleExceptionInternal(baseResponse);
    }

    /**
     * 클라이언트 에러
     * @param generalException
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> onThrowException(BaseException generalException) {
        Reason.ReasonDto errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorReasonHttpStatus.getCode(), errorReasonHttpStatus.getMessage(), null);
        return handleExceptionInternal(baseResponse);
    }

    private ResponseEntity<Object> handleExceptionInternal(ApiResponse<?> response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCommonStatus) {
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}