package com.onetool.server.member;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.exception.MemberNotFoundException;
import com.onetool.server.member.dto.LoginRequest;
import com.onetool.server.member.dto.MemberCreateRequest;
import com.onetool.server.member.dto.MemberCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public MemberCreateResponse createMember(MemberCreateRequest request) {
        Member member = memberRepository.save(request.toEntity());
        return MemberCreateResponse.of(member);
    }

    public String login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        log.info("============== 로그인 유저 정보 ===============");
        log.info(member.toString());

        if(!encoder.matches(password, member.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        MemberAuthContext context = MemberAuthContext.builder()
                .id(member.getId())
                .role(member.getRole().name())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();

        return jwtUtil.create(context);
    }
}
