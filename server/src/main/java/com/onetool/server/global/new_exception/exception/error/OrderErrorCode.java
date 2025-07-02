package com.onetool.server.global.new_exception.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@Getter
public enum OrderErrorCode implements ErrorCodeIfs{

    //결제 및 주문 에러
    ORDER_NOT_FOUND(NOT_FOUND, "ORDER-001", "주문이 존재하지 않습니다."),
    NULL_POINT_ERROR(HttpStatus.NOT_FOUND, "ORDER-0010", "해당 객체는 NULL입니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "ORDER-0011", "해당 객체는 서버에 존재하지 않습니다"),
    DEPOSIT_NOT_FOUND(NOT_FOUND, "PAYMENT-001", "결제가 존재하지 않습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String serverCode;
    private final String description;
}
