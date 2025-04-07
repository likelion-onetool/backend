package com.onetool.server.api.member.business;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.dto.command.MemberCreateCommand;
import com.onetool.server.api.member.dto.command.MemberUpdateCommand;
import com.onetool.server.api.member.dto.request.MemberCreateRequest;
import com.onetool.server.api.member.dto.request.MemberFindEmailRequest;
import com.onetool.server.api.member.dto.request.MemberUpdateRequest;
import com.onetool.server.api.member.dto.response.BlueprintDownloadResponse;
import com.onetool.server.api.member.dto.response.MemberCreateResponse;
import com.onetool.server.api.member.dto.response.MemberInfoResponse;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.global.annotation.Business;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
@Slf4j
public class MemberBusiness {

    private final PasswordEncoder encoder;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public String findEmail(String name, String phoneNum) {
        return memberService.findOne(name, phoneNum);
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse findMemberInfo(Long userId) {
        Member member = memberService.findOne(userId);
        return MemberInfoResponse.from(member);
    }

    @Transactional(readOnly = true)
    public List<BlueprintDownloadResponse> findPurchasedBlueprints(final Long userId) {
        final Member member = memberService.findOne(userId);
        return member.getOrderBlueprints()
                .stream().map(BlueprintDownloadResponse::from)
                .toList();
    }

    @Transactional
    public MemberCreateResponse createMember(MemberCreateCommand command) {
        memberService.validateDuplicateEmail(command.email());
        Member member = memberService.save(command.toEntity(encoder.encode(command.password())));

        log.info("createMember(): {}", member.getEmail());
        return MemberCreateResponse.of(member);
    }

    @Transactional
    public void updateMember(MemberUpdateCommand command) {
        Member member = memberService.findOne(command.id());
        member.update(command, encoder);
        memberService.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberService.findOne(id);
        memberService.delete(member);
    }
}
