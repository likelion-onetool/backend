package com.onetool.server.api.member.fixture;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.dto.request.MemberCreateRequest;
import com.onetool.server.api.member.dto.request.MemberUpdateRequest;
import com.onetool.server.api.member.dto.response.BlueprintDownloadResponse;
import com.onetool.server.api.member.dto.response.MemberCreateResponse;
import com.onetool.server.api.member.enums.UserRole;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;

import java.util.*;

public class MemberFixture {

    public static Member createAdmin() {
        return Member.builder()
                .id(1L)
                .name("관리자 윤성원")
                .email("admin@example.com")
                .role(UserRole.ROLE_ADMIN)
                .phoneNum("01012345678")
                .field("BACKEND")
                .isNative(true)
                .build();
    }

    public static Member createMember() {
        return Member.builder()
                .id(2L)
                .name("홍길동")
                .email("user@example.com")
                .role(UserRole.ROLE_ADMIN)
                .phoneNum("01000000000")
                .field("BACKEND")
                .isNative(true)
                .orders(new ArrayList<>())
                .build();
    }

    public static MemberCreateRequest createMemberCreateRequest() {
        return new MemberCreateRequest(
                "user@example.com",
                "1234",
                "홍길동",
                "2024-01-01",
                "BACKEND",
                "01000000000",
                true
        );
    }

    public static MemberCreateResponse createMemberCreateResponse() {
        return MemberCreateResponse.builder()
                .id(2L)
                .name("홍길동")
                .email("user@example.com")
                .build();
    }

    public static MemberUpdateRequest createMemberUpdateRequest() {
        return MemberUpdateRequest.builder()
                .name("길동홍")
                .build();
    }

    public static List<QnaBoardBriefResponse> createQnaBoardBriefResponses() {
        List<QnaBoardBriefResponse> responses = new ArrayList<>();
        responses.add(QnaBoardBriefResponse.builder().title("test1").writer("writer1").build());
        responses.add(QnaBoardBriefResponse.builder().title("test2").writer("writer2").build());
        return responses;
    }

    public static List<BlueprintDownloadResponse> createBlueprintDownloadResponses() {
        List<BlueprintDownloadResponse> responses = new ArrayList<>();
        responses.add(BlueprintDownloadResponse.builder().blueprintId(1L).blueprintName("도면1").build());
        responses.add(BlueprintDownloadResponse.builder().blueprintId(2L).blueprintName("도면2").build());
        return responses;
    }
}
