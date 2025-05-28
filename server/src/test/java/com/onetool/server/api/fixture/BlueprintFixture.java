package com.onetool.server.api.fixture;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.dto.request.BlueprintRequest;
import com.onetool.server.api.blueprint.dto.request.BlueprintUpdateRequest;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.response.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlueprintFixture {

    public static BlueprintRequest createBlueprintRequest() {
        return BlueprintRequest.builder()
                .blueprintName("대한민국 마을")
                .blueprintDetails("대한민국의 어느 마을의 청사진입니다.")
                .creatorName("원툴")
                .standardPrice(50000L)
                .salePrice(50000L)
                .extension(".exe")
                .program("CAD")
                .downloadLink("https://onetool.com/download")
                .blueprintImg("https://s3.bucket.image.com/")
                .categoryId(1L)
                .saleExpiredDate(LocalDateTime.now().plusDays(10))
                .detailImage("detailImage URL")
                .build();
    }

    public static BlueprintUpdateRequest createBlueprintUpdateRequest() {
        return BlueprintUpdateRequest.builder()
                .id(1L)
                .blueprintName("대한민국 마을")
                .creatorName("원툴")
                .build();
    }

    public static List<BlueprintResponse> createBlueprintResponseList() {
        return List.of(
                createBlueprintResponse(1L, "골프장 1인실 평면도(1)"),
                createBlueprintResponse(1L, "골프장 레이아웃 평면도(1)"),
                createBlueprintResponse(1L, "나만의 자취방 6평"),
                createBlueprintResponse(1L, "100평 영화관 평면도 & 레이아웃 ver1.0.1")
        );
    }

    public static Page<Blueprint> createBlueprintPage() {
        List<Blueprint> blueprints = new ArrayList<>();
        blueprints.add(createCustomBlueprint("테스트도면1", "원툴", InspectionStatus.NONE));
        blueprints.add(createCustomBlueprint("테스트도면2", "원툴", InspectionStatus.NONE));
        blueprints.add(createCustomBlueprint("테스트도면3", "원툴", InspectionStatus.NONE));

        return new PageImpl<>(blueprints);
    }

    public static BlueprintResponse createBlueprintResponse(final long id, final String blueprintName) {
        return BlueprintResponse.builder()
                .id(id)
                .blueprintName(blueprintName)
                .build();
    }

    public static List<SearchResponse> createSearchResponseList() {
        return List.of(
                createSearchResponse(1L, "골프장 1인실 평면도(1)"),
                createSearchResponse(2L, "골프장 레이아웃 평면도(1)")
        );
    }

    public static Page<SearchResponse> createSearchResponsePage() {
        return new PageImpl<>(createSearchResponseList());
    }

    public static Blueprint createBlueprint() {
        return Blueprint.builder()
                .id(1L)
                .blueprintName("생각나는대로 만든 건축물1")
                .creatorName("원툴")
                .categoryId(1L)
                .secondCategory("공공")
                .inspectionStatus(InspectionStatus.NONE)
                .build();
    }

    private static Blueprint createCustomBlueprint(String blueprintName, String creatorName, InspectionStatus status) {
        return Blueprint.builder()
                .id(1L)
                .blueprintName(blueprintName)
                .creatorName(creatorName)
                .categoryId(1L)
                .secondCategory("공공")
                .inspectionStatus(status)
                .build();
    }

    private static SearchResponse createSearchResponse(final long id, final String blueprintName) {
        return SearchResponse.builder()
                .id(id)
                .blueprintName(blueprintName)
                .creatorName("원툴")
                .categoryId(1L)
                .secondCategory("공공")
                .build();
    }
}
