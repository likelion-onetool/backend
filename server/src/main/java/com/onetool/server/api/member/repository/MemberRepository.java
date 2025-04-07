package com.onetool.server.api.member.repository;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.enums.SocialType;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    void delete(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    boolean existsById(Long id);

    int count();

    Optional<Member> findByEmail(String email);

    Long countAllMember();

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String id);

    Optional<Member> findByIdWithCart(Long id);

    Optional<String> findByNameAndPhoneNum(String name, String phoneNum);

    boolean existsByEmail(String email);

    Optional<Member> findMemberById(Long id);
}
