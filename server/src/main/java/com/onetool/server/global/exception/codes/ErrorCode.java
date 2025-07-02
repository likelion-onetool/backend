package com.onetool.server.global.exception.codes;

import com.onetool.server.global.exception.codes.reason.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "G001", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(HttpStatus.BAD_REQUEST, "G002", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "G003", " Invalid Type Value"),

    DUPLICATE(HttpStatus.BAD_REQUEST, "G004", "Duplicate Key"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "G004", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(HttpStatus.BAD_REQUEST, "G005", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(HttpStatus.BAD_REQUEST, "G007", "com.fasterxml.jackson.core Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(FORBIDDEN, "G008", "Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(NOT_FOUND, "G009", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(NOT_FOUND, "G010", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(NOT_FOUND, "G011", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(NOT_FOUND, "G012", "Header에 데이터가 존재하지 않는 경우 "),

    // 4xx : client error

    //사용자 에러
    NON_EXIST_USER(NOT_FOUND, "MEMBER-0001", "존재하지 않는 회원입니다"),
    ILLEGAL_LOGOUT_USER(HttpStatus.BAD_REQUEST, "MEMBER-0002", "이미 로그아웃된 회원입니다."),

    //바인딩 에러
    BINDING_ERROR(HttpStatus.BAD_REQUEST, "BINDING-0000", "바인딩에 실패했습니다."),

    //로그인 에러
    EMAIL_NOT_FOUND(NOT_FOUND, "LOGIN-0001", "이메일이 잘못됨"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-0000", "잘못된 요청입니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "COMMON-0002", "이미 존재하는 회원입니다."),
    DUPLICATE_MEMBER(HttpStatus.BAD_REQUEST, "COMMON-0003", "기존 회원 정보와 중복됩니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "COMMON-0004", "유효하지 않은 토큰입니다."),

    //도면 에러
    NO_BLUEPRINT_FOUND(NOT_FOUND, "BLUEPRINT-0000", "도면이 존재하지 않습니다."),
    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "BLUEPRINT-0001", "정렬 타입이 올바르지 않습니다."),
    BLUEPRINT_NOT_APPROVED(HttpStatus.BAD_REQUEST, "BLUEPRINT-0002", "승인되지 않은 도면입니다."),

    //카테고리 에러
    CATEGORY_NOT_FOUND(NOT_FOUND, "CATEGORY-0000", "존재하지 않는 카테고리입니다."),

    //장바구니 에러
    NO_ITEM_IN_CART(NO_CONTENT, "CART-0000", "장바구니에 상품이 없습니다."),
    ALREADY_EXIST_BLUEPRINT_IN_CART(HttpStatus.BAD_REQUEST, "CART-0001", "장바구니에 존재하는 상품입니다."),
    CART_BLUEPRINT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CART-0002", "장바구니에 담긴 도면을 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.BAD_REQUEST, "CART-0003", "장바구니를 찾을 수 없습니다."),

    //Qna 에러
//    NO_QNA_CONTENT(HttpStatus.NO_CONTENT, "QNA-0000", "게시된 문의사항이 없습니다."),
    UNAVAILABLE_TO_MODIFY(FORBIDDEN, "QNA-0001", "게시글에 대한 권한이 없습니다."),
    NO_QNA_REPLY(NO_CONTENT, "QNA-0002", "유효한 댓글이 아닙니다."),

    //결제 및 주문 에러
    ORDER_NOT_FOUND(NOT_FOUND, "ORDER-001", "주문이 존재하지 않습니다."),
    DEPOSIT_NOT_FOUND(NOT_FOUND, "PAYMENT-001", "결제가 존재하지 않습니다."),

    // 5xx : server error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-0000", "서버 에러"),

    // 이메일 관련
    EMAIL_ERROR(HttpStatus.BAD_REQUEST, "EMAIL-0001", "이메일 전송 중 오류가 발생했습니다."),
    AUTH_CODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL-0002", "인증코드 생성 중 문제가 발생했습니다."),
    RANDOM_PASSWORD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL-0003", "랜덤 패스워드 생성 중 문제가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public Reason.ReasonDto getReasonHttpStatus() {
        return Reason.ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}