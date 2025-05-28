package com.onetool.server.api.payments.service;

import com.onetool.server.api.order.Order;
import com.onetool.server.api.payments.domain.Payment;
import com.onetool.server.api.payments.repository.PaymentRepository;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.PaymentErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public Payment findByOrders(Order order) {
        return paymentRepository.findByOrders(order);
    }

    @Transactional(readOnly = true)
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() ->
                new ApiException(PaymentErrorCode.PAYMENT_NOT_FOUND,"ID와 일치하는 결제 정보가 존재하지 않습니다."));
    }

    @Transactional
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Transactional
    public void deleteById(Payment payment) {
        paymentRepository.delete(payment);
    }
}
