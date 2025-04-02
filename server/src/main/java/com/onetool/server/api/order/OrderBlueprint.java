package com.onetool.server.api.order;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Builder
@Entity(name = "order_blueprint")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE order_blueprint SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class OrderBlueprint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blueprint_id")
    private Blueprint blueprint;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    private String downloadUrl;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public OrderBlueprint(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    //연관관계 맺기~~~~~~
    public void assignOrder(Order order) {
        if (this.order != order) {
            this.order = order;
            order.getOrderItems().add(this);
        }
    }

    public void assignBlueprint(Blueprint blueprint) {
        if (this.blueprint != blueprint) {
            this.blueprint = blueprint;
            blueprint.getOrderBlueprints().add(this);
        }
    }
}