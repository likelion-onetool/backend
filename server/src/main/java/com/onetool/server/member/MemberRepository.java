package com.onetool.server.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query(value = "SELECT count(*) FROM Member")
    Long countAllMember();

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String id);
}
