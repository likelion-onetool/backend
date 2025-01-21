package com.onetool.server.global.auth.login.service;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.exception.InvalidTokenException;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberRepository;
import com.onetool.server.global.auth.login.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

        MemberAuthContext context = MemberAuthContext.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole().name())
                .password(member.getPassword())
                .name(member.getName())
                .build();

        return new PrincipalDetails(context);
    }

    public void saveUserInSecurityContext(String memberId, String token) {
        if (memberId == null) {
            throw new InvalidTokenException();
        }

        PrincipalDetails principalDetails = loadUserByUsername(memberId);
        Collection<? extends GrantedAuthority> authorities = principalDetails.getAuthorities();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principalDetails, token, authorities);

        if(auth != null) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
        }
    }
}
