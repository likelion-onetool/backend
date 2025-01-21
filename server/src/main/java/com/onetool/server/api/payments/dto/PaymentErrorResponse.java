package com.onetool.server.api.payments.dto;

import lombok.Builder;

@Builder
public record PaymentErrorResponse(
        int code,
        String message
) {

}
