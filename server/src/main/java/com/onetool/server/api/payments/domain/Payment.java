package com.onetool.server.api.payments.domain;

import com.onetool.server.api.order.Order;
import com.onetool.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE payment SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;
    private String accountNumber;
    private String bankName;
    private Long totalPrice;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
