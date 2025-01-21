package com.onetool.server.api.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {
    private String email;
    private String name;
    private String phoneNum;
    private String developmentField;
    private String currentPassword;
    private String newPassword;

    @Builder
    public MemberUpdateRequest(String email, String name, String phoneNum, String developmentField, String currentPassword, String newPassword) {
        this.email = email;
        this.name = name;
        this.phoneNum = phoneNum;
        this.developmentField = developmentField;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}