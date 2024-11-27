package com.onetool.server.api.order;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity (name = "order_blueprint")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderBlueprint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //도면관련 매핑은 좀 이따가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blueprint_id")
    private Blueprint blueprint;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "orders_id")
    private Orders order;


}