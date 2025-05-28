package com.onetool.server.api.member.repository;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.enums.SocialType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
@Primary
public interface MemberJpaRepository extends MemberRepository, Repository<Member, Long> {

    Member save(Member member);

    void delete(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    boolean existsById(Long id);

    int count();

    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query(value = "SELECT count(*) FROM Member m")
    Long countAllMember();

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String id);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.cart WHERE m.id = :id")
    Optional<Member> findByIdWithCart(@Param("id") Long id);

    @Query("SELECT m.email FROM Member m WHERE m.name = :name AND m.phoneNum = :phoneNum")
    Optional<String> findByNameAndPhoneNum(@Param("name") String name, @Param("phoneNum") String phoneNum);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m WHERE m.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.cart WHERE m.id = :id")
    Optional<Member> findMemberById(@Param("id") Long id);
}
