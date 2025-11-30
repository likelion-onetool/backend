package com.onetool.server.data;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.category.FirstCategoryRepository;
import com.onetool.server.api.member.repository.MemberJpaRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Profile({"local"})
@Component
@ConditionalOnProperty(name = "dummy.data.loader.enabled", havingValue = "true")
public class DummyDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final static int DUMMY_DATA_COUNT = 1_000_000;

    private final static String[] BLUEPRINT_NAME_LIST = {"골프장 ", "당구장 ", "학교 ", "영화관 ", "도서관 ", "학교 ", "상가 ", "전시관", "역사관 ", "식당 "};
    private final static String BLUEPRINT_DETAIL = "도면에 대한 상세 정보 내역입니다.";
    private final static String CREATOR_NAME = "작가 ";
    private final static Long STANDARD_PRICE = 10000L;
    private final static Long SALE_PRICE = 10000L;
    private final static String PROGRAM = "CAD";
    private final static String DOWNLOAD_LINK = "https://google.com";
    private final static String EXTENSION = ".exe";
    private final static String BLUEPRINT_IMG = "https://s3.amazon.com/dd2gA1fgdaQ51df52134";
    private final static BigInteger HITS = BigInteger.valueOf(0);
    private final static InspectionStatus INSPECTION_STATUS = InspectionStatus.PASSED;

    private final FirstCategoryRepository firstCategoryRepository;
    private final BlueprintRepository blueprintRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public DummyDataLoader(FirstCategoryRepository firstCategoryRepository, BlueprintRepository blueprintRepository, MemberJpaRepository memberJpaRepository, PasswordEncoder passwordEncoder) {
        this.firstCategoryRepository = firstCategoryRepository;
        this.blueprintRepository = blueprintRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDummyData();
    }

    private void createDummyData() {
        for(int i = 0; i < DUMMY_DATA_COUNT; i++) {
            createBlueprint(
                    BLUEPRINT_NAME_LIST[i%10],
                    CREATOR_NAME + i,
                    (long) (i%6)
            );
        }
    }

    private void createBlueprint(
            String blueprintName,
            String creatorName,
            Long categoryId
    ) {
        Blueprint blueprint = Blueprint.builder()
                .blueprintName(blueprintName)
                .blueprintDetails(DummyDataLoader.BLUEPRINT_DETAIL)
                .creatorName(creatorName)
                .standardPrice(DummyDataLoader.STANDARD_PRICE)
                .salePrice(DummyDataLoader.SALE_PRICE)
                .program(DummyDataLoader.PROGRAM)
                .downloadLink(DummyDataLoader.DOWNLOAD_LINK)
                .extension(DummyDataLoader.EXTENSION)
                .blueprintImg(DummyDataLoader.BLUEPRINT_IMG)
                .categoryId(categoryId)
                .secondCategory("공공")
                .hits(DummyDataLoader.HITS)
                .inspectionStatus(DummyDataLoader.INSPECTION_STATUS)
                .build();
        blueprintRepository.save(blueprint);
    }
}
