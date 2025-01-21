package com.onetool.server;

import com.onetool.server.blueprint.Blueprint;
import com.onetool.server.blueprint.repository.BlueprintRepository;
import com.onetool.server.category.FirstCategory;
import com.onetool.server.category.FirstCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("default")
@Component
public class DataLoader implements CommandLineRunner {

    private final FirstCategoryRepository firstCategoryRepository;
    private final BlueprintRepository blueprintRepository;

    public DataLoader(FirstCategoryRepository firstCategoryRepository, BlueprintRepository blueprintRepository) {
        this.firstCategoryRepository = firstCategoryRepository;
        this.blueprintRepository = blueprintRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        final FirstCategory firstCategory1 = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(1L)
                        .name("building")
                        .build()
        );

        final FirstCategory firstCategory2 = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(2L)
                        .name("civil")
                        .build()
        );

        final FirstCategory firstCategory3 = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(3L)
                        .name("interior")
                        .build()
        );

        final FirstCategory firstCategory4 = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(4L)
                        .name("machine")
                        .build()
        );

        final FirstCategory firstCategory5 = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(5L)
                        .name("electric")
                        .build()
        );

        final FirstCategory firstCategory6 = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(6L)
                        .name("etc")
                        .build()
        );

        createBlueprint(
                "건축 도면 1",
                "주거 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                1L,
                "주거"
        );
        createBlueprint(
                "건축 도면 2",
                "건축 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                1L,
                "주거"
        );
        createBlueprint(
                "건축 도면 3",
                "건축 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                1L,
                "상업"
        );
        createBlueprint(
                "건축 도면 4",
                "건축 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                1L,
                "상업"
        );
        createBlueprint(
                "건축 도면 5",
                "건축 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                1L,
                "공공"
        );

        // 2. 토목도면

        createBlueprint(
                "토목 도면 1",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "도로"
        );
        createBlueprint(
                "토목 도면 2",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "도로"
        );
        createBlueprint(
                "토목 도면 3",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "도로"
        );
        createBlueprint(
                "토목 도면 4",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "교량"
        );
        createBlueprint(
                "토목 도면 5",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "교량"
        );
        createBlueprint(
                "토목 도면 6",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "터널"
        );
        createBlueprint(
                "토목 도면 7",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "터널"
        );

        createBlueprint(
                "토목 도면 8",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "댐/수자원"
        );
        createBlueprint(
                "토목 도면 9",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "댐/수자원"
        );
        createBlueprint(
                "토목 도면 10",
                "토목 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                2L,
                "댐/수자원"
        );

        /*
         * 3. 인테리어 도면
         */
        createBlueprint(
                "인테리어 도면 1",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "주거"
        );
        createBlueprint(
                "인테리어 도면 2",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "주거"
        );
        createBlueprint(
                "인테리어 도면 3",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "상업"
        );
        createBlueprint(
                "인테리어 도면 4",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "상업"
        );
        createBlueprint(
                "인테리어 도면 5",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "상업"
        );

        createBlueprint(
                "인테리어 도면 6",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "가구/집기"
        );
        createBlueprint(
                "인테리어 도면 7",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "가구/집기"
        );
        createBlueprint(
                "인테리어 도면 8",
                "인테리어 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                3L,
                "가구/집기"
        );

        /*
         * 4. 기계 도면
         */

        createBlueprint(
                "기계 도면 1",
                "기계 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                4L,
                "기계부품"
        );
        createBlueprint(
                "기계 도면 2",
                "기계 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                4L,
                "기계부품"
        );
        createBlueprint(
                "기계 도면 3",
                "기계 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                4L,
                "기계부품"
        );
        createBlueprint(
                "기계 도면 4",
                "기계 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                4L,
                "기계부품"
        );
        createBlueprint(
                "기계 도면 5",
                "기계 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                4L,
                "기계부품"
        );

        /*
         * 5. 전기 도면
         */
        createBlueprint(
                "전기 도면 1",
                "전기 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                5L,
                "전기"
        );
        createBlueprint(
                "전기 도면 2",
                "전기 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                5L,
                "전기"
        );
        createBlueprint(
                "전기 도면 3",
                "전기 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                5L,
                "전기"
        );
        createBlueprint(
                "전기 도면 4",
                "전기 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                5L,
                "전기"
        );
        createBlueprint(
                "전기 도면 5",
                "전기 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                5L,
                "전기"
        );

        /*
         * 6. 기타 도면
         */
        createBlueprint(
                "기타 도면 1",
                "기타 도면에 대한 설명",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://onetool.com/download",
                ".exe",
                "https://s3.bucket.image.com/",
                6L,
                null
        );
    }

    private void createBlueprint(
            String blueprintName,
            String blueprintDetails,
            String creatorName,
            Long standardPrice,
            Long salePrice,
            String program,
            String downloadLink,
            String extension,
            String blueprintImg,
            Long categoryId,
            String secondCategory
    ) {
        Blueprint blueprint = Blueprint.builder()
                .blueprintName(blueprintName)
                .blueprintDetails(blueprintDetails)
                .creatorName(creatorName)
                .standardPrice(standardPrice)
                .salePrice(salePrice)
                .program(program)
                .downloadLink(downloadLink)
                .extension(extension)
                .blueprintImg(blueprintImg)
                .categoryId(categoryId)
                .secondCategory(secondCategory)
                .build();
        blueprintRepository.save(blueprint);
    }
}