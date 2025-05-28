package com.onetool.server.api.member.fake;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.enums.SocialType;
import com.onetool.server.api.member.repository.MemberJpaRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemberJpaRepository implements MemberJpaRepository {

    private final Map<Long, Member> store = new HashMap<>();

    @Override
    public Member save(Member member) {
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public void delete(Member member) {
        store.remove(member.getId());
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    @Override
    public int count() {
        return store.size();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return store.values().stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Long countAllMember() {
        return (long) store.size();
    }

    @Override
    public Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String id) {
        return store.values().stream()
                .filter(m -> m.getSocialType() == socialType && m.getSocialId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Member> findByIdWithCart(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<String> findByNameAndPhoneNum(String name, String phoneNum) {
        return store.values().stream()
                .filter(m -> m.getName().equals(name) && m.getPhoneNum().equals(phoneNum))
                .findFirst()
                .map(Member::getEmail);
    }

    @Override
    public boolean existsByEmail(String email) {
        return store.values().stream()
                .anyMatch(m -> m.getEmail().equals(email));
    }

    @Override
    public Optional<Member> findMemberById(Long id) {
        return Optional.empty();
    }

    public void clear() {
        store.clear();
    }
}
