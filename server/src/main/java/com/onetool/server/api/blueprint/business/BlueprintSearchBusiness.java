package com.onetool.server.api.blueprint.business;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.dto.response.BlueprintResponse;
import com.onetool.server.api.blueprint.dto.response.BlueprintSortRequest;
import com.onetool.server.api.blueprint.dto.response.SearchResponse;
import com.onetool.server.api.blueprint.service.BlueprintSearchService;
import com.onetool.server.api.category.FirstCategoryType;
import com.onetool.server.global.annotation.Business;
import com.onetool.server.global.exception.BlueprintNotApprovedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.List;

@Business
@RequiredArgsConstructor
public class BlueprintSearchBusiness {

    private final BlueprintSearchService blueprintSearchService;

    @Transactional
    public Page<SearchResponse> getSearchResponsePage(String keyword, Pageable pageable) {
        Page<Blueprint> blueprintPage = blueprintSearchService.findAllByPassed(keyword, pageable);

        return makeBlueprintPage(pageable, blueprintPage.getContent(), blueprintPage);
    }

    @Transactional
    public Page<SearchResponse> getSearchResponsePage(FirstCategoryType firstCategory, String secondCategory, Pageable pageable) {
        Page<Blueprint> blueprintPage = (secondCategory == null)
                ? blueprintSearchService.findAllByPassed(firstCategory.getCategoryId(), pageable)
                : blueprintSearchService.findAllByPassed(firstCategory.getCategoryId(), secondCategory, pageable);

        return makeBlueprintPage(pageable, blueprintPage.getContent(), blueprintPage);
    }

    @Transactional
    public Page<SearchResponse> getSearchResponsePage(Pageable pageable) {
        Page<Blueprint> blueprintPage = blueprintSearchService.findAllByPassed(pageable);

        return makeBlueprintPage(pageable, blueprintPage.getContent(), blueprintPage);
    }

    @Transactional
    public BlueprintResponse getApprovedBlueprintResponse(Long blueprintId) {
        Blueprint blueprint = blueprintSearchService.findOne(blueprintId);
        if (blueprint.getInspectionStatus() != InspectionStatus.PASSED) {
            throw new BlueprintNotApprovedException(blueprintId.toString());
        }

        return BlueprintResponse.from(blueprint);
    }

    @Transactional
    public List<BlueprintResponse> getSortedBluePrintList(BlueprintSortRequest request, Pageable sortedPageable) {
        Long categoryId = getCategoryId(request.categoryName());
        FirstCategoryType category = (categoryId != null) ? FirstCategoryType.findByCategoryId(categoryId) : null;
        Page<Blueprint> blueprintPage = (category == null)
                ? blueprintSearchService.findAllByPassed(sortedPageable)
                : blueprintSearchService.findAllByPassed(category.getCategoryId(), sortedPageable);

        return BlueprintResponse.toBlueprintResponseList(blueprintPage.getContent());
    }

    private PageImpl<SearchResponse> makeBlueprintPage(Pageable pageable, List<Blueprint> blueprintList, Page<Blueprint> blueprintPage) {
        List<SearchResponse> searchResponseList = SearchResponse.toSearchResponseList(blueprintList);

        return new PageImpl<>(searchResponseList, pageable, blueprintPage.getTotalElements());
    }

    private Long getCategoryId(String categoryName) {
        if (categoryName == null) {
            return null;
        }
        return FirstCategoryType.findByType(categoryName).getCategoryId();
    }
}
