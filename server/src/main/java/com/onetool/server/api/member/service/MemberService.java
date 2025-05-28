package com.onetool.server.api.member.service;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberRepository;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public String findOne(String name, String phoneNumber) {
        return memberRepository.findByNameAndPhoneNum(name, phoneNumber).orElseThrow(() ->
                new ApiException(MemberErrorCode.NON_EXIST_USER, "이름과 비밀번호가 일치하는 회원이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member findOne(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(MemberErrorCode.NON_EXIST_USER, "이메일과 일치하는 회원이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member findOne(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
                new ApiException(MemberErrorCode.NON_EXIST_USER, "ID가 일치하는 회원이 존재하지 않습니다."));
    }

    @Transactional
    public void delete(Member member) {
        memberRepository.delete(member);
    }

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public void updatePassword(Member member, String encodedNewPassword) {
        member.update(encodedNewPassword);
        memberRepository.save(member);
    }

    public void validateExistId(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ApiException(MemberErrorCode.NON_EXIST_USER, "ID가 일치하는 회원이 존재하지 않습니다.");
        }
    }

    public void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ApiException(MemberErrorCode.EXIST_EMAIL, "이미 사용 중인 이메일입니다.");
        }
    }

    public Member findOneWithCart(Long id) {
        return memberRepository
                .findByIdWithCart(id)
                .orElseThrow(() -> new ApiException(MemberErrorCode.NON_EXIST_USER));
    }
}
