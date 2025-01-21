package com.onetool.server.api.payments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onetool.server.api.payments.domain.TossPayment;
import com.onetool.server.api.payments.domain.TossPaymentMethod;
import com.onetool.server.api.payments.domain.TossPaymentStatus;
import com.onetool.server.api.payments.repository.PaymentRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void savePayment(JSONObject response) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        String method = (String) response.get("method");
        String paymentKey = (String) response.get("paymentKey");
        String orderId = (String) response.get("orderId");
        LocalDateTime requestedAt = LocalDateTime.parse((String) response.get("requestedAt"), formatter);
        LocalDateTime approvedAt = LocalDateTime.parse((String) response.get("approvedAt"), formatter);
        Long totalAmount = (Long) response.get("totalAmount");
        String status = (String) response.get("status");

        TossPayment tossPayment = TossPayment.builder()
                .tossPaymentKey(paymentKey)
                .tossOrderId(orderId)
                .tossPaymentMethod(TossPaymentMethod.valueOf(method))
                .tossPaymentStatus(TossPaymentStatus.valueOf(status))
                .requestedAt(requestedAt)
                .approvedAt(approvedAt)
                .totalAmount(totalAmount)
                .build();

        paymentRepository.save(tossPayment);
    }
}
