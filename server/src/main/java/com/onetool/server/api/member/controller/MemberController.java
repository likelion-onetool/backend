package com.onetool.server.api.member.controller;

import com.onetool.server.api.member.business.MemberBusiness;
import com.onetool.server.api.member.dto.*;
import com.onetool.server.api.qna.business.QnaBoardBusiness;
import com.onetool.server.api.qna.dto.response.QnaBoardBriefResponse;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.exception.codes.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

    private final MemberBusiness memberBusiness;
    private final QnaBoardBusiness qnaBoardBusiness;

    @PostMapping("/signup")
    public ApiResponse<?> createMember(@RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = memberBusiness.createMember(request);
        return ApiResponse.of(SuccessCode.CREATED, response);
    }

    @PatchMapping
    public ApiResponse<?> updateMember(
            @Valid @RequestBody MemberUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Long id = principalDetails.getContext().getId();
        memberBusiness.updateMember(id, request);
        return ApiResponse.onSuccess("회원 정보가 수정되었습니다.");
    }

    @DeleteMapping
    public ApiResponse<?> deleteMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long id = principalDetails.getContext().getId();
        memberBusiness.deleteMember(id);
        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }

    @GetMapping
    public ApiResponse<?> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getContext().getId();
        MemberInfoResponse memberResponse = memberBusiness.getMemberInfo(memberId);
        return ApiResponse.onSuccess(memberResponse);
    }

    @GetMapping("/myQna")
    public ApiResponse<List<QnaBoardBriefResponse>> getMyQna(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.onSuccess(qnaBoardBusiness.getMyQna(principalDetails.getContext()));
    }

    @GetMapping("/myPurchase")
    public ApiResponse<List<BlueprintDownloadResponse>> getMyPurchases(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getContext().getId();
        List<BlueprintDownloadResponse> blueprints = memberBusiness.getPurchasedBlueprints(userId);
        return ApiResponse.onSuccess(blueprints);
    }
}