package com.onetool.server.global.exception.codes;

import com.onetool.server.global.exception.codes.reason.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode {

    // 4xx : client error

    //url 에러
    URL_NON_EXIST(HttpStatus.NOT_FOUND, "URL ERROR", "잘못된 경로입니다."),

    //사용자 에러
    NON_EXIST_USER(HttpStatus.NOT_FOUND, "MEMBER-0001", "존재하지 않는 회원입니다"),
    ILLEGAL_LOGOUT_USER(HttpStatus.BAD_REQUEST, "MEMBER-0002", "이미 로그아웃된 회원입니다."),

    //바인딩 에러
    BINDING_ERROR(HttpStatus.BAD_REQUEST, "BINDING-0000", "바인딩에 실패했습니다."),

    //로그인 에러
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "LOGIN-0001", "이메일이 잘못됨"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-0000", "잘못된 요청입니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "COMMON-0002", "이미 존재하는 회원입니다."),

    //도면 에러
    NO_BLUEPRINT_FOUND(HttpStatus.NOT_FOUND, "BLUEPRINT-0000", "도면이 존재하지 않습니다."),
    BLUEPRINT_FILE_NECESSARY(HttpStatus.BAD_REQUEST, "BLUEPRINT-0001", "도면 파일을 업로드해주세요."),
    BLUEPRINT_FILE_EXTENSION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "BLUEPRINT-0002", "도면 파일 확장자가 올바르지 않습니다."),

    //카테고리 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY-0000", "존재하지 않는 카테고리입니다."),

    //카테고리 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY-0000", "존재하지 않는 카테고리입니다."),

    //장바구니 에러
    NO_ITEM_IN_CART(HttpStatus.NO_CONTENT, "CART-0000", "장바구니에 상품이 없습니다."),
    ALREADY_EXIST_BLUEPRINT_IN_CART(HttpStatus.BAD_REQUEST, "CART-0001", "장바구니에 존재하는 상품입니다."),

    //Qna 에러
    NO_QNA_CONTENT(HttpStatus.NO_CONTENT, "QNA-0000", "게시된 문의사항이 없습니다."),
    UNAVAILABLE_TO_MODIFY(HttpStatus.FORBIDDEN, "QNA-0001", "게시글에 대한 권한이 없습니다."),
    NO_QNA_REPLY(HttpStatus.NO_CONTENT, "QNA-0002", "유효한 댓글이 아닙니다."),
    /*
    *
    * */

    // 5xx : server error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-0000", "서버 에러");

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