package com.onetool.server.api.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "이메일이 null 일 수 없습니다.")
    @Email
    private String email;

    @NotNull(message = "비밀번호는 null 일 수 없습니다.")
    private String password;
}
