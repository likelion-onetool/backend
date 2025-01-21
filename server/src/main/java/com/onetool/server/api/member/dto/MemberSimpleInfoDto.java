package com.onetool.server.api.member.dto;

import com.onetool.server.api.member.domain.Member;

public record MemberSimpleInfoDto(
        String name,
        String email,
        String phoneNum
) {
    public static MemberSimpleInfoDto makeMemberSimpInfoDto(Member member){
        return new MemberSimpleInfoDto(
                member.getName(),
                member.getEmail(),
                member.getPhoneNum()
        );
    }
}