package com.onetool.server.api.member.dto;

public record MemberFindEmailRequest(
        String name,
        String phone_num
) {
}