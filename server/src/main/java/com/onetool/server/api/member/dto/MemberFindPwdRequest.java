package com.onetool.server.api.member.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Setter
@Getter
@NoArgsConstructor
public class MemberFindPwdRequest {

    private String email;

    @Builder
    public MemberFindPwdRequest(String email) {
        this.email = email;
    }
}
