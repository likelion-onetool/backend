package com.onetool.server.global.new_exception.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@AllArgsConstructor
@Getter
public enum CartErrorCode {

    //장바구니 에러
    NO_ITEM_IN_CART(NO_CONTENT, "CART-0000", "장바구니에 상품이 없습니다."),
    ALREADY_EXIST_BLUEPRINT_IN_CART(HttpStatus.BAD_REQUEST, "CART-0001", "장바구니에 존재하는 상품입니다."),
    CART_BLUEPRINT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CART-0002", "장바구니에 담긴 도면을 찾을 수 없습니다."),
    CART_NOT_FOUND(HttpStatus.BAD_REQUEST, "CART-0003", "장바구니를 찾을 수 없습니다."),
    ;
    
    private final HttpStatus httpStatus;
    private final String serverCode;
    private final String description;
}
