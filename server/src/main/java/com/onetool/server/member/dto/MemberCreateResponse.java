package com.onetool.server.member.dto;

import com.onetool.server.member.Member;
import lombok.Builder;

public record MemberCreateResponse(
        Long id,
        String email,
        String name
) {

    @Builder
    public MemberCreateResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static MemberCreateResponse of(Member member) {
        return MemberCreateResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
