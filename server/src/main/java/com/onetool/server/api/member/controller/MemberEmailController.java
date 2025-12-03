package com.onetool.server.api.member.controller;

import com.onetool.server.api.member.business.MemberBusiness;
import com.onetool.server.api.member.business.MemberEmailBusiness;
import com.onetool.server.api.member.dto.request.MemberFindEmailRequest;
import com.onetool.server.api.member.dto.request.MemberFindPwdRequest;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 - 이메일", description = "아이디/비밀번호 찾기, 이메일 인증 등 이메일 관련 API")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users/email")
public class MemberEmailController {

    private final MemberBusiness memberBusiness;
    private final MemberEmailBusiness memberEmailBusiness;

    @Operation(summary = "아이디(이메일) 찾기 API", description = "회원 이름과 전화번호로 아이디(이메일)를 찾습니다.")
    @PostMapping("/find-email")
    public ApiResponse<?> findEmail(@RequestBody MemberFindEmailRequest request) {
        String email = memberBusiness.findEmail(request.name(), request.phone_num());
        return ApiResponse.onSuccess(email);
    }

    @Operation(summary = "비밀번호 찾기(임시 비밀번호 발급) API", description = "가입된 이메일로 임시 비밀번호를 발송합니다.")
    @PostMapping("/find-password")
    public ApiResponse<?> findPwdCheck(@RequestBody MemberFindPwdRequest request) {
        memberEmailBusiness.findLostPwd(request.getEmail());
        return ApiResponse.onSuccess("이메일을 발송했습니다.");
    }

    @Operation(summary = "이메일 인증 코드 발송 API", description = "회원가입 시 이메일 인증을 위해 인증 코드를 발송합니다.")
    @PostMapping("/verification-requests")
    public ApiResponse<?> sendMessage(@RequestParam("email") String email) {
        log.info("request email: {}", email);
        memberEmailBusiness.sendCodeToEmail(email);
        return ApiResponse.onSuccess("이메일이 발송되었습니다.");
    }

    @Operation(summary = "이메일 인증 코드 확인 API", description = "입력된 이메일과 인증 코드가 유효한지 확인합니다.")
    @GetMapping("/verifications")
    public ApiResponse<?> verificationEmail(
            @RequestParam("email") @Valid String email,
            @RequestParam("code") String authCode
    ) {
        boolean response = memberEmailBusiness.verifiedCode(email, authCode);
        return (response) ? ApiResponse.onSuccess("이메일이 인증되었습니다.")
                : ApiResponse.onFailure("404", "코드가 일치하지 않습니다.", null);
    }
}
