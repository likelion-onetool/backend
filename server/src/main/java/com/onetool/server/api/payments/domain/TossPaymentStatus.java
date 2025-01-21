package com.onetool.server.api.payments.domain;

public enum TossPaymentStatus {
    ABORTED,
    CANCELED,
    DONE,
    EXPIRED,
    IN_PROGRESS,
    PARTIAL_CANCELED,
    READY,
    WAITING_FOR_DEPOSIT;

    @Override
    public String toString() {
        return this.name();
    }
}
