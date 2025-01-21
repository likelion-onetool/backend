package com.onetool.server.api.order;

import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.payments.Payments;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    private int totalCount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderBlueprint> orderItems = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_member_id")
    private Member member;

    @OneToOne
    private Payments payments;

}