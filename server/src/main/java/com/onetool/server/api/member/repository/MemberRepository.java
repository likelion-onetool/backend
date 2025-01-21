package com.onetool.server.api.member.repository;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT count(*) FROM Member")
    Long countAllMember();

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String id);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.cart WHERE m.id = :id")
    Optional<Member> findByIdWithCart(@Param("id") Long id);

    Optional<Member> findByNameAndPhoneNum(String name, String phoneNum);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m JOIN FETCH m.qnaBoards WHERE m.id = :memberId")
    Optional<Member> findMemberWithQnaBoards(@Param("memberId") Long memberId);
}