package com.onetool.server.api.payments.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
public enum TossPaymentMethod {
    가상계좌,
    간편결제,
    게임문화상품권,
    계좌이체,
    도서문화상품권,
    문화상품권,
    카드,
    휴대폰;

    @Override
    public String toString() {
        return this.name();
    }


}
