package com.onetool.server.member.dto;

import com.onetool.server.member.domain.Member;

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
