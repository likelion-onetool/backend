package com.onetool.server.global.exception;

import com.onetool.server.global.exception.codes.BaseCode;

public class CategoryNotFoundException extends BaseException {
    public CategoryNotFoundException(BaseCode code) {
        super(code);
    }
}