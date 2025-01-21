package com.onetool.server;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.blueprint.InspectionStatus;
import com.onetool.server.api.blueprint.repository.BlueprintRepository;
import com.onetool.server.api.category.FirstCategory;
import com.onetool.server.api.category.FirstCategoryRepository;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.enums.UserRole;
import com.onetool.server.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Profile({"dev", "default"})
@Component
public class DataLoader implements CommandLineRunner {

    private final FirstCategoryRepository firstCategoryRepository;
    private final BlueprintRepository blueprintRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(FirstCategoryRepository firstCategoryRepository, BlueprintRepository blueprintRepository, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.firstCategoryRepository = firstCategoryRepository;
        this.blueprintRepository = blueprintRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        if(memberRepository.count() == 0) {
            createDummyData();
        }
    }

    private void createDummyData() {
        Member member = memberRepository.save(
                Member.builder()
                        .name("관리자1")
                        .password(passwordEncoder.encode("1234"))
                        .email("admin@admin.com")
                        .role(UserRole.ROLE_ADMIN)
                        .build()
        );

        final FirstCategory buildingCategory = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(1L)
                        .name("building")
                        .build()
        );

        final FirstCategory civilCategory = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(2L)
                        .name("civil")
                        .build()
        );

        final FirstCategory interiorCategory = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(3L)
                        .name("interior")
                        .build()
        );

        final FirstCategory machineCategory = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(4L)
                        .name("machine")
                        .build()
        );

        final FirstCategory electricCategory = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(5L)
                        .name("electric")
                        .build()
        );

        final FirstCategory etcCategory = firstCategoryRepository.save(
                FirstCategory.builder()
                        .id(6L)
                        .name("etc")
                        .build()
        );

        createBlueprint(
                "골프장 1인실 평면도(1)",
                "1인실 용 골프장 평면도 1번 타입 입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1vklVlR8HmxONiF6DGgzuOHeNMZ0dX52o/view?usp=drive_link",
                ".exe",
                "https://static.wixstatic.com/media/7f0bf6_d3bf447c412d4adbb2a6f194152e6cf6~mv2.png/v1/fill/w_1000,h_682,al_c,q_90,usm_0.66_1.00_0.01/7f0bf6_d3bf447c412d4adbb2a6f194152e6cf6~mv2.png",
                buildingCategory.getId(),
                "공공",
                BigInteger.valueOf(0),
                InspectionStatus.PASSED
        );
        createBlueprint(
                "골프장 1인실 평면도(2)",
                "1인실 용 골프장 평면도 2번 타입 입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1CxunkC9lwLkhT7-R1DcKYZCRluTFLh-m/view?usp=drive_link",
                ".exe",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjJgbMbq-26Z5xmG29MddfVwxNzij9VIql8A&s",
                buildingCategory.getId(),
                "공공",
                BigInteger.valueOf(0),
                InspectionStatus.NONE
        );
        createBlueprint(
                "골프장 레이아웃 평면도(1)",
                "골프장 레이아웃 평면도 1번 타입 입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1viyFD9GIDoVkez3x1FuubSlO_FNWvLjM/view?usp=drive_link",
                ".exe",
                "https://d2v80xjmx68n4w.cloudfront.net/members/portfolios/wDxN41662466397.png?w=500",
                buildingCategory.getId(),
                "공공",
                BigInteger.valueOf(0),
                InspectionStatus.PASSED
        );
        createBlueprint(
                "골프장 레이아웃 평면도(2)",
                "골프장 레이아웃 평면도 2번 타입 입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1RNuvEm4Qs6FTTYouTT5AqGXuucNoU4J4/view?usp=drive_link",
                ".exe",
                "https://d2v80xjmx68n4w.cloudfront.net/members/portfolios/9gyy61718952150.jpg?w=500",
                buildingCategory.getId(),
                "공공",
                BigInteger.valueOf(0),
                InspectionStatus.PASSED
        );
        createBlueprint(
                "골프장 레이아웃 평면도(3)",
                "골프장 레이아웃 평면도 3번 타입 입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1FNHH-94O3qv_ME4ifW_9ZPVTucskZ_H4/view?usp=drive_link",
                ".exe",
                "https://i0.wp.com/916er.com/wp-content/uploads/KakaoTalk_20210616_091912636.png?resize=840%2C472&ssl=1",
                buildingCategory.getId(),
                "공공",
                BigInteger.valueOf(0),
                InspectionStatus.PASSED
        );
        createBlueprint(
                "골프장 인테리어 평면도",
                "골프장 인테리어에 대한 평면도입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1UDJ12d6jFaxda2J6NZTT8cHRR4jcsVO8/view?usp=drive_link",
                ".exe",
                "https://d2v80xjmx68n4w.cloudfront.net/members/portfolios/rXf2u1654730039.jpg?w=500",
                interiorCategory.getId(),
                "상업",
                BigInteger.valueOf(0),
                InspectionStatus.PASSED
        );
        createBlueprint(
                "보라동 삼정 아파트",
                "보라동 삼정 아파트의 평면도 입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1Pjznqsc1cljoYCvUfqsomUmfnic1eijc/view?usp=drive_link",
                ".exe",
                "https://mblogthumb-phinf.pstatic.net/MjAxOTA2MThfMzgg/MDAxNTYwODI2MjIzNTg3.J6ZwiB_n0W9nb81eMnBhhT1H2Nn4kLMqdCI1oCvMmq0g.Iim4ORXK0DqNWGFQQCyjxbLrKqSgCLFlXZUCkR-O9y4g.JPEG.nexuscon0318/%EC%82%BC%EC%A0%95%ED%8F%89%EB%A9%B4.jpg?type=w420",
                interiorCategory.getId(),
                "상업",
                BigInteger.valueOf(0),
                InspectionStatus.NONE
        );
        createBlueprint(
                "스튜디오 평면도",
                "개인 스튜디오의 평면도 입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1JUY_ONXqwP6k3BxX1QjOFOJfV9hSB1Zc/view?usp=drive_link",
                ".exe",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLEUeCyucb-aXWd3Vkw_vFR5e7DbsrLxJruw&s",
                interiorCategory.getId(),
                "상업",
                BigInteger.valueOf(0),
                InspectionStatus.PASSED
        );
        createBlueprint(
                "유닛 2&4",
                "유닛 2, 4 평면도 모음입니다.",
                "원툴",
                50000L,
                40000L,
                "CAD",
                "https://drive.google.com/file/d/1sUqogEZnNgsLuf9eP4goL6FynRAY1F4d/view?usp=drive_link",
                ".exe",
                "https://godomall.speedycdn.net/233e03d1d27f28fca50cf28c8e1f8429/goods/1000000582/image/detail/thumb/register_detail_578.jpg",
                etcCategory.getId(),
                "상업",
                BigInteger.valueOf(0),
                InspectionStatus.PASSED
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
            String secondCategory,
            BigInteger hits,
            InspectionStatus inspectionStatus
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
                .hits(hits)
                .inspectionStatus(inspectionStatus)
                .build();
        blueprintRepository.save(blueprint);
    }
}