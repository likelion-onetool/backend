package com.onetool.server.api.payments.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PaymentProperties {
    @Value("${toss.payment.secret-key}")
    private String secretKey;
    @Value("${toss.payment.confirm-url}")
    private String confirmURL;
}