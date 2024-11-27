package com.onetool.server.api.blueprint.controller;

import com.onetool.server.api.blueprint.dto.SearchResponse;
import com.onetool.server.api.blueprint.service.BlueprintService;
import com.onetool.server.api.category.FirstCategoryType;
import com.onetool.server.global.exception.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private BlueprintService blueprintService;

    public SearchController(BlueprintService blueprintService) {
        this.blueprintService = blueprintService;
    }

    @GetMapping("/blueprint")
    public ApiResponse<Page<SearchResponse>> searchWithKeyword(
            @RequestParam("s")String keyword,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
    ) {

        Page<SearchResponse> response = blueprintService.searchNameAndCreatorWithKeyword(keyword, pageable);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/blueprint/building")
    public ApiResponse<Page<SearchResponse>> searchBuildingCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintService.findAllByCategory(FirstCategoryType.CATEGORY_BUILDING, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/civil")
    public ApiResponse<Page<SearchResponse>> searchCivilCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintService.findAllByCategory(FirstCategoryType.CATEGORY_CIVIL, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/interior")
    public ApiResponse<Page<SearchResponse>> searchInteriorCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintService.findAllByCategory(FirstCategoryType.CATEGORY_INTERIOR, category, pageable);
        return ApiResponse.onSuccess(responses);
    }
    @GetMapping("/blueprint/machine")
    public ApiResponse<Page<SearchResponse>> searchMachineCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintService.findAllByCategory(FirstCategoryType.CATEGORY_MACHINE, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/electric")
    public ApiResponse<Page<SearchResponse>> searchElectricCategory(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false) String category
    ) {
        Page<SearchResponse> responses = blueprintService.findAllByCategory(FirstCategoryType.CATEGORY_ELECTRIC, category, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/etc")
    public ApiResponse<Page<SearchResponse>> searchEtcCategory(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
    ) {
        Page<SearchResponse> responses = blueprintService.findAllByCategory(FirstCategoryType.CATEGORY_CIVIL, null, pageable);
        return ApiResponse.onSuccess(responses);
    }

    @GetMapping("/blueprint/all")
    public ApiResponse<Page<SearchResponse>> searchAllBlueprint(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
    ) {
        Page<SearchResponse> responses = blueprintService.findAll(pageable);
        return ApiResponse.onSuccess(responses);
    }
}