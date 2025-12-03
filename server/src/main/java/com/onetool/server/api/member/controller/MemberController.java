package com.onetool.server.api.member.controller;

import com.onetool.server.api.member.business.MemberBusiness;
import com.onetool.server.api.member.dto.command.MemberCreateCommand;
import com.onetool.server.api.member.dto.command.MemberUpdateCommand;
import com.onetool.server.api.member.dto.request.MemberCreateRequest;
import com.onetool.server.api.member.dto.request.MemberUpdateRequest;
import com.onetool.server.api.member.dto.response.BlueprintDownloadResponse;
import com.onetool.server.api.member.dto.response.MemberCreateResponse;
import com.onetool.server.api.member.dto.response.MemberInfoResponse;
import com.onetool.server.api.qna.business.QnaBoardBusiness;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.exception.codes.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "회원", description = "회원 정보 및 활동 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

    private final MemberBusiness memberBusiness;
    private final QnaBoardBusiness qnaBoardBusiness;

    @Operation(summary = "회원가입 API", description = "새로운 회원을 등록합니다.")
    @PostMapping("/signup")
    public ApiResponse<?> createMember(@RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = memberBusiness.createMember(MemberCreateCommand.from(request));
        return ApiResponse.of(SuccessCode.CREATED, response);
    }

    @Operation(summary = "회원 정보 수정 API", description = "현재 로그인된 사용자의 정보를 수정합니다.")
    @PatchMapping
    public ApiResponse<?> updateMember(
            @Valid @RequestBody MemberUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Long id = principalDetails.getContext().getId();
        memberBusiness.updateMember(MemberUpdateCommand.from(id, request));
        return ApiResponse.onSuccess("회원 정보가 수정되었습니다.");
    }

    @Operation(summary = "회원 탈퇴 API", description = "현재 로그인된 사용자를 탈퇴 처리합니다.")
    @DeleteMapping
    public ApiResponse<?> deleteMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long id = principalDetails.getContext().getId();
        memberBusiness.deleteMember(id);
        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }

    @Operation(summary = "내 정보 조회 API", description = "현재 로그인된 사용자의 정보를 조회합니다.")
    @GetMapping
    public ApiResponse<?> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getContext().getId();
        MemberInfoResponse memberResponse = memberBusiness.findMemberInfo(memberId);
        return ApiResponse.onSuccess(memberResponse);
    }

    @Operation(summary = "내가 작성한 Q&A 목록 조회 API", description = "현재 로그인된 사용자가 작성한 Q&A 게시글 목록을 조회합니다.")
    @GetMapping("/myQna")
    public ApiResponse<List<QnaBoardBriefResponse>> getMyQna(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long id = principalDetails.getContext().getId();
        return ApiResponse.onSuccess(qnaBoardBusiness.getMyQna(id));
    }

    @Operation(summary = "내 구매 목록 조회 API", description = "현재 로그인된 사용자가 구매한 도면 목록을 조회합니다.")
    @GetMapping("/myPurchase")
    public ApiResponse<List<BlueprintDownloadResponse>> getMyPurchases(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long id = principalDetails.getContext().getId();
        List<BlueprintDownloadResponse> blueprints = memberBusiness.findPurchasedBlueprints(id);
        return ApiResponse.onSuccess(blueprints);
    }
}
