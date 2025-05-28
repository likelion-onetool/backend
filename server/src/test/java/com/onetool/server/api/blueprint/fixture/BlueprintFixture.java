package com.onetool.server.api.blueprint.fixture;

import com.onetool.server.api.blueprint.Blueprint;

import java.util.ArrayList;

public class BlueprintFixture {

    public static Blueprint createBluePrint(Long id){
        return Blueprint.builder()
                .id(id)
                .blueprintName("테스트도면")
                .creatorName("테스트도면작성자")
                .downloadLink("Test.uri")
                .salePrice(1000L)
                .standardPrice(10000L)
                .orderBlueprints(new ArrayList<>())
                .build();
    }
}
