package com.onetool.server.api.member.dto;

import com.onetool.server.api.member.enums.UserRole;
import lombok.Builder;

public record LoginMember(
        Long id,
        String email,
        String name,
        String password,
        UserRole role
) {

    @Builder
    public LoginMember(Long id, String email, String name, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }
}
