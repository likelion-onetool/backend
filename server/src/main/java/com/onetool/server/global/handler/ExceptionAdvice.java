package com.onetool.server.global.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.onetool.server.global.exception.*;
import com.onetool.server.global.exception.base.BaseException;
import com.onetool.server.global.exception.base.DuplicateException;
import com.onetool.server.global.exception.base.NotFoundException;
import com.onetool.server.global.exception.codes.BaseCode;
import com.onetool.server.global.exception.codes.ErrorCode;
import com.onetool.server.global.exception.codes.reason.Reason;
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
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice {

    /**
     * 바인딩 에러 처리
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler
    public ResponseEntity<Object> validation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ApiResponse<?> baseResponse = ApiResponse.onFailure(ErrorCode.BINDING_ERROR.getCode(), message, null);
        return handleExceptionInternal(baseResponse);
    }

    /**
     * 중복 예외 처리
     * @param e DuplicateException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleDuplicateException(DuplicateException e) {
        Reason.ReasonDto errorReasonHttpStatus = e.getErrorReasonHttpStatus();
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorReasonHttpStatus.getCode(), errorReasonHttpStatus.getMessage(), e.getMessage());
        return handleExceptionInternal(baseResponse);
    }

    /**
     * 존재하지 않는 예외 처리
     * @param e NotFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        Reason.ReasonDto errorReasonHttpStatus = e.getErrorReasonHttpStatus();
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorReasonHttpStatus.getCode(), errorReasonHttpStatus.getMessage(), e.getMessage());
        return handleExceptionInternal(baseResponse);
    }

    /**
     * 서버 에러
     * @param e Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e) {
        log.error(e.getMessage(), e);
        ErrorCode errorCode = ErrorCode._INTERNAL_SERVER_ERROR;
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null);
        return handleExceptionInternal(baseResponse);
    }

    /**
     * 클라이언트 에러
     * @param generalException BaseException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> onThrowException(BaseException generalException) {
        Reason.ReasonDto errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorReasonHttpStatus.getCode(), errorReasonHttpStatus.getMessage(), null);
        return handleExceptionInternal(baseResponse);
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);

        return handleExceptionInternal(ErrorCode.REQUEST_BODY_MISSING_ERROR);
    }

    /**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.info("HttpMessageNotReadableException", ex);
        return handleExceptionInternal(ErrorCode.REQUEST_BODY_MISSING_ERROR);
    }

    /**
     * [Exception] 클라이언트에서 request로 '파라미터로' 데이터가 넘어오지 않았을 경우
     *
     * @param ex MissingServletRequestParameterException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        return handleExceptionInternal(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR);
    }


    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param e HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<Object> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);
        return handleExceptionInternal(ErrorCode.BAD_REQUEST_ERROR);
    }


    /**
     * [Exception] NULL 값이 발생한 경우
     *
     * @param e NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        return handleExceptionInternal(ErrorCode.NULL_POINT_ERROR);
    }

    /**
     * Input / Output 내에서 발생한 경우
     *
     * @param ex IOException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOException(IOException ex) {
        log.error("handleIOException", ex);
        return handleExceptionInternal(ErrorCode.IO_ERROR);
    }

    /**
     * com.fasterxml.jackson.core 내에 Exception 발생하는 경우
     *
     * @param ex JsonProcessingException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        return handleExceptionInternal(ErrorCode.REQUEST_BODY_MISSING_ERROR);
    }

    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param e NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundExceptionException", e);
        return handleExceptionInternal(ErrorCode.NOT_FOUND_ERROR);
    }

    private ResponseEntity<Object> handleExceptionInternal(ApiResponse<?> response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCommonStatus) {
        ApiResponse<?> baseResponse = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}