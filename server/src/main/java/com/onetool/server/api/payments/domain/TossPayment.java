package com.onetool.server.api.payments.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onetool.server.api.order.Orders;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TossPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String tossPaymentKey;

    @Column(unique = false)
    String tossOrderId;

    long totalAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    TossPaymentMethod tossPaymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    TossPaymentStatus tossPaymentStatus;

    LocalDateTime requestedAt;
    LocalDateTime approvedAt;
}
