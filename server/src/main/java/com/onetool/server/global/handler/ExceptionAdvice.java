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
    public ApiResponse<?> InvalidSortTypeException(InvalidSortTypeException e) {
        return ApiResponse.onFailure("400", "올바르지 않은 정렬 방식입니다.", null);
    }

    @ExceptionHandler(BlueprintNotApprovedException.class)
    public ApiResponse<?> BlueprintNotApprovedException(BlueprintNotApprovedException e) {
        return ApiResponse.onFailure("403", "승인받지 않은 도면입니다.", null);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ApiResponse<?> CategoryNotFoundException(CategoryNotFoundException e) {
        return ApiResponse.onFailure("404", "존재하지 않는 카테고리입니다.", null);
    }

    /**
     * 서버 에러 처리
     */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Object> exception(Exception e) {
        log.error(e.getMessage(), e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR; // _INTERNAL_SERVER_ERROR 대신 사용
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null);
        return handleExceptionInternal(baseResponse);
    }

    /**
     * 클라이언트 에러 처리
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> onThrowException(BaseException generalException) {
        log.error("BaseException", generalException);
        Reason.ReasonDto errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorReasonHttpStatus.getCode(), errorReasonHttpStatus.getMessage(), null);
        return handleExceptionInternal(baseResponse);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        return handleExceptionInternal(ErrorCode.REQUEST_BODY_MISSING_ERROR);
    }

    /**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.info("HttpMessageNotReadableException", ex);
        return handleExceptionInternal(ErrorCode.REQUEST_BODY_MISSING_ERROR);
    }

    /**
     * [Exception] 클라이언트에서 request로 '파라미터로' 데이터가 넘어오지 않았을 경우
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        return handleExceptionInternal(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR);
    }

    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<Object> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);
        return handleExceptionInternal(ErrorCode.BAD_REQUEST_ERROR);
    }

    /**
     * [Exception] NULL 값이 발생한 경우
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        return handleExceptionInternal(ErrorCode.NULL_POINT_ERROR);
    }

    /**
     * Input / Output 내에서 발생한 경우
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOException(IOException ex) {
        log.error("handleIOException", ex);
        return handleExceptionInternal(ErrorCode.IO_ERROR);
    }

    /**
     * com.fasterxml.jackson.core 내에 Exception 발생하는 경우
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        return handleExceptionInternal(ErrorCode.REQUEST_BODY_MISSING_ERROR);
    }

    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundExceptionException", e);
        return handleExceptionInternal(ErrorCode.NOT_FOUND_ERROR);
    }

    /**
     * 공통적인 예외 처리를 위한 내부 메서드
     */
    private ResponseEntity<Object> handleExceptionInternal(ApiResponse<?> response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCommonStatus) {
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}