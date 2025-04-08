package com.onetool.server.api.order.fixture;

import com.onetool.server.api.order.OrderBlueprint;

public class OrderBlueprintFixture {

    public static OrderBlueprint createOrderBlueprint(Long id){
        return OrderBlueprint.builder()
                .id(id)
                .downloadUrl("Test.url")
                .build();
    }
}
