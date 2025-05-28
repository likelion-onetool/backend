package com.onetool.server.api.order;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.payments.domain.Payment;
import com.onetool.server.api.payments.domain.PaymentStatus;
import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.api.member.domain.Member;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@SQLDelete(sql = "UPDATE Orders SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Slf4j
@Table(name ="orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    private int totalCount;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PAY_PENDING'")
    private PaymentStatus status;

    @BatchSize(size = 100)
    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    private List<OrderBlueprint> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Payment payment;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public Order(List<Blueprint> blueprints) {
        this.totalCount = blueprints.size();
        this.totalPrice = calcTotalPrice(blueprints);
    }

    private Long calcTotalPrice(List<Blueprint> blueprints) {
        return blueprints.stream().mapToLong(Blueprint::getStandardPrice).sum();
    }

    public List<String> getOrderItemsDownloadLinks() {
        log.info("DownLoadList start~");
        List<String> orderItemsDownloadLinks = new ArrayList<>();
        if (this.getStatus() == PaymentStatus.PAY_DONE) {

            this.orderItems.forEach(orderBlueprint ->
                    orderItemsDownloadLinks.add(
                            orderBlueprint.getDownloadUrl())
            );
        }
        log.info("DownLoadList end~");

        return orderItemsDownloadLinks;
    }

    public void assignMember(Member member) {
        if (this.member != member) {
            this.member = member;
            member.getOrders().add(this);
        }
    }

    public static List<OrderBlueprint> getOrderItems(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .toList();
    }
}