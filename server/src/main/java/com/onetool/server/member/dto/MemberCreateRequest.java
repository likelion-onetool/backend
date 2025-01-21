package com.onetool.server.member.dto;

import com.onetool.server.member.domain.DateTimeFormat;
import com.onetool.server.member.domain.Member;
import com.onetool.server.member.enums.UserRole;
import lombok.Builder;

import java.time.LocalDate;

public record MemberCreateRequest(
        String email,
        String password,
        String name,
        String birthDate,
        String development_field,
        String phoneNum,
        boolean isNative
) {
    @Builder
    public MemberCreateRequest(String email, String password, String name, String birthDate, String development_field, String phoneNum, boolean isNative) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.development_field = development_field;
        this.phoneNum = phoneNum;
        this.isNative = isNative;
    }

    public Member toEntity(String encoded) {
        return Member.builder()
                .email(email)
                .role(UserRole.ROLE_USER)
                .password(encoded)
                .birthDate(LocalDate.parse(birthDate, DateTimeFormat.dateFormat))
                .name(name)
                .isNative(isNative)
                .field(development_field)
                .phoneNum(phoneNum)
                .build();
    }
}
