package com.onetool.server.global.exception;

import com.onetool.server.global.exception.codes.BaseCode;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException(BaseCode code) {
        super(code);
    }
}
