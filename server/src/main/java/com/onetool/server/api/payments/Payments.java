package com.onetool.server.api.payments;

import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.api.order.Orders;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payments extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int paymentAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentType;
    private String paymentRequestAt;
    private String paymentApprovedAt;
    private String paymentLastTransactionKey;

    @OneToOne
    private Orders order;
}
