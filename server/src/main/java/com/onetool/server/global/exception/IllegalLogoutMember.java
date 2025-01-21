package com.onetool.server.global.exception;

import com.onetool.server.global.exception.codes.BaseCode;

public class IllegalLogoutMember extends BaseException {
    public IllegalLogoutMember(BaseCode code) {
        super(code);
    }
}
