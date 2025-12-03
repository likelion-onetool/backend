package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.business.BlueprintSearchBusiness;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.response.BlueprintSortRequest;
import com.onetool.server.api.blueprint.dto.response.SearchResponse;
import com.onetool.server.api.blueprint.enums.SortType;
import com.onetool.server.api.category.FirstCategoryType;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "도면 검색", description = "도면 검색 및 조회 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final BlueprintSearchBusiness blueprintSearchBusiness;

    @Operation(summary = "키워드 기반 도면 검색 API", description = "키워드를 포함하는 도면을 검색합니다.")
    @GetMapping("/blueprint")
    public ApiResponse<Page<SearchResponse>> searchWithKeyword(
            @RequestParam("s") String keyword,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        String decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        log.info("Decoded keyword: {}", decodedKeyword);
        Page<SearchResponse> response = blueprintSearchBusiness.getSearchResponsePage(decodedKeyword, pageable);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "건축 카테고리 도면 검색 API", description = "건축 카테고리에 속하는 도면을 검색합니다.")
    @GetMapping("/blueprint/building")
    public ApiResponse<Page<SearchResponse>> searchBuildingCategory(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchBusiness.getSearchResponsePage(FirstCategoryType.CATEGORY_BUILDING, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "토목 카테고리 도면 검색 API", description = "토목 카테고리에 속하는 도면을 검색합니다.")
    @GetMapping("/blueprint/civil")
    public ApiResponse<Page<SearchResponse>> searchCivilCategory(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchBusiness.getSearchResponsePage(FirstCategoryType.CATEGORY_CIVIL, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "인테리어 카테고리 도면 검색 API", description = "인테리어 카테고리에 속하는 도면을 검색합니다.")
    @GetMapping("/blueprint/interior")
    public ApiResponse<Page<SearchResponse>> searchInteriorCategory(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchBusiness.getSearchResponsePage(FirstCategoryType.CATEGORY_INTERIOR, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "기계 카테고리 도면 검색 API", description = "기계 카테고리에 속하는 도면을 검색합니다.")
    @GetMapping("/blueprint/machine")
    public ApiResponse<Page<SearchResponse>> searchMachineCategory(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchBusiness.getSearchResponsePage(FirstCategoryType.CATEGORY_MACHINE, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "전기 카테고리 도면 검색 API", description = "전기 카테고리에 속하는 도면을 검색합니다.")
    @GetMapping("/blueprint/electric")
    public ApiResponse<Page<SearchResponse>> searchElectricCategory(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintSearchBusiness.getSearchResponsePage(FirstCategoryType.CATEGORY_ELECTRIC, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "기타 카테고리 도면 검색 API", description = "기타 카테고리에 속하는 도면을 검색합니다.")
    @GetMapping("/blueprint/etc")
    public ApiResponse<Page<SearchResponse>> searchEtcCategory(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SearchResponse> responses = blueprintSearchBusiness.getSearchResponsePage(FirstCategoryType.CATEGORY_CIVIL, null, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "모든 도면 조회 API", description = "모든 도면을 페이지별로 조회합니다.")
    @GetMapping("/blueprint/all")
    public ApiResponse<Page<SearchResponse>> searchAllBlueprint(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SearchResponse> responses = blueprintSearchBusiness.getSearchResponsePage(pageable);
        return ApiResponse.onSuccess(responses);
    }

    @Operation(summary = "도면 상세 정보 조회 API", description = "특정 도면의 상세 정보를 조회합니다.")
    @GetMapping("/blueprint/{id}")
    public ApiResponse<BlueprintResponse> getBlueprintDetails(@PathVariable("id") Long id) {
        BlueprintResponse blueprintResponseDTO = blueprintSearchBusiness.getApprovedBlueprintResponse(id);
        return ApiResponse.onSuccess(blueprintResponseDTO);
    }

    @Operation(summary = "도면 정렬 API", description = "카테고리별 또는 전체 도면을 특정 기준으로 정렬하여 조회합니다.")
    @GetMapping({"/blueprint/sort", "/blueprint/{categoryName}/sort"})
    public ApiResponse<List<BlueprintResponse>> sortBlueprints(
            @PathVariable(name = "categoryName", required = false) String categoryName,
            @RequestParam(name = "sortBy") String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
            Pageable pageable
    ) {
        BlueprintSortRequest request = new BlueprintSortRequest(categoryName, sortBy, sortOrder);
        Sort sort = SortType.getSortBySortType(SortType.valueOf(request.sortBy().toUpperCase()), request.sortOrder());
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        List<BlueprintResponse> sortedItems = blueprintSearchBusiness.getSortedBluePrintList(request, sortedPageable);
        return ApiResponse.onSuccess(sortedItems);
    }
}
