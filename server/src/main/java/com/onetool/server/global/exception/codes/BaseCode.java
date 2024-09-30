package com.onetool.server.global.exception.codes;

import com.onetool.server.global.exception.codes.reason.Reason;

public interface BaseCode {
    Reason.ReasonDto getReasonHttpStatus();
}
