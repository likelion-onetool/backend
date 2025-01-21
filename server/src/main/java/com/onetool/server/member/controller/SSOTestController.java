package com.onetool.server.member.controller;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Slf4j
@Controller
public class SSOTestController {

    @GetMapping("/login/sso")
    public String gotoSsoLoginPage() {
        return "login";
    }

    @GetMapping("/principal")
    public ResponseEntity getPrincipal(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info(principalDetails.toString());
        return ResponseEntity.ok(principalDetails);
    }
}
