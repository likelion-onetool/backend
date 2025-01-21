package com.onetool.server.api.member.controller;

import com.onetool.server.api.member.dto.*;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.exception.MemberNotFoundException;
import com.onetool.server.global.exception.codes.SuccessCode;
import com.onetool.server.member.dto.*;
import com.onetool.server.api.qna.dto.response.QnaBoardResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ApiResponse<MemberLoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        Map<String, String> tokens = memberService.login(request);
        MemberLoginResponse response = MemberLoginResponse.builder()
                .accessToken("Bearer " + tokens.get("accessToken"))
                .refreshToken(tokens.get("refreshToken"))
                .build();
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/signup")
    public ApiResponse<?> createMember(@RequestBody MemberCreateRequest request) {
        MemberCreateResponse response = memberService.createMember(request);
        return ApiResponse.of(SuccessCode.CREATED, response);
    }

    @PostMapping("/email")
    public  ApiResponse<?> findEmail(@RequestBody MemberFindEmailRequest request) {
        String email = memberService.findEmail(request);
        return ApiResponse.onSuccess(email);
    }

    @PostMapping("/emails/verification-requests")
    public ApiResponse<?> sendMessage(@RequestParam("email") @Valid String email) {
        memberService.sendCodeToEmail(email);
        return ApiResponse.onSuccess("이메일이 발송되었습니다.");
    }

    @GetMapping("/emails/verifications")
    public ApiResponse<?> verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("code") String authCode) {
        boolean response = memberService.verifiedCode(email, authCode);
        return (response) ? ApiResponse.onSuccess("이메일이 인증되었습니다.")
                : ApiResponse.onFailure("404", "코드가 일치하지 않습니다.", null);
    }

    @PostMapping("/password")
    public ApiResponse<?> findPwdCheck(@RequestBody MemberFindPwdRequest request) {
        boolean successFlag = memberService.findLostPwd(request);
        if(successFlag) {
            return ApiResponse.onSuccess("이메일을 발송했습니다.");
        } else {
            return ApiResponse.onFailure("403", "이메일 발송 과정에서 오류가 발생했습니다.", null);
        }
    }

    @PatchMapping
    public ApiResponse<?> updateMember(
            @Valid @RequestBody MemberUpdateRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if(principalDetails == null) {
            throw new MemberNotFoundException();
        }

        Long id = principalDetails.getContext().getId();
        memberService.updateMember(id, request);

        return ApiResponse.onSuccess("회원 정보가 수정되었습니다.");
    }

    @DeleteMapping
    public ApiResponse<?> deleteMember(
           @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {

        if(principalDetails == null) {
            throw new MemberNotFoundException();
        }


        Long id = principalDetails.getContext().getId();
        memberService.deleteMember(id);

        return ApiResponse.onSuccess("회원 탈퇴가 완료되었습니다.");
    }

    @GetMapping
    public ApiResponse<?> getMemberInfo(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if(principalDetails == null) {
            throw new MemberNotFoundException();
        }

        Long id = principalDetails.getContext().getId();

        try {
            MemberInfoResponse memberResponse = memberService.getMemberInfo(id);
            return ApiResponse.onSuccess(memberResponse);
        } catch (RuntimeException e) {
            return ApiResponse.onFailure("404", "회원을 찾을 수 없습니다.", null);
        }
    }

    // TODO : uri 수정 필요
    @GetMapping("/myPage/myQna")
    public ApiResponse<List<QnaBoardResponse.QnaBoardBriefResponse>> getMyQna(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.onSuccess(memberService.findQnaWrittenById(principalDetails.getContext()));
    }

    @GetMapping("/myPurchase")
    public ApiResponse<List<BlueprintDownloadResponse>> getMyPurchases(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getContext().getId();
        List<BlueprintDownloadResponse> blueprints = memberService.getPurchasedBlueprints(userId);
        return ApiResponse.onSuccess(blueprints);
    }
}