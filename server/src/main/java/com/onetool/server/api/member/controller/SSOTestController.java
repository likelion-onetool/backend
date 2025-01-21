package com.onetool.server.api.member.controller;

import com.onetool.server.global.auth.login.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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