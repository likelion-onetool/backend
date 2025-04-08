package com.onetool.server.api.payments.dto;

import com.onetool.server.api.order.service.OrderService;
import com.onetool.server.api.payments.domain.Payment;
import lombok.Builder;

import java.util.Set;

@Builder
public record PaymentRequest(
        Long orderId,
        String accountName,
        String accountNumber,
        String bankName,
        Long totalPrice,
        Set<Long> blueprintIds
) {
    public Payment toEntity(PaymentRequest request, OrderService orderService) {
        return Payment.builder()
                .accountNumber(request.accountNumber())
                .accountName(request.accountName())
                .bankName(request.bankName())
                .totalPrice(request.totalPrice())
                .order(orderService.findOne(request.orderId()))
                .build();
    }
}
