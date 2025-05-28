package com.onetool.server.api.fixture;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.enums.SocialType;
import com.onetool.server.api.member.enums.UserRole;
import com.onetool.server.api.cart.Cart;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MemberFixture {

    private static Member createRandomMember(Long id) {
        return Member.builder()
                .id(null)  // ID는 보통 자동 생성됨
                .password("password" + id)  // ID별로 다른 비밀번호
                .email("user" + id + "@example.com")  // ID별로 다른 이메일
                .name("User" + id)  // ID별로 다른 이름
                .birthDate(LocalDate.of(1990 + (int) (id % 10), 1, 1))  // 1990~1999년 사이 랜덤 생년월일
                .phoneNum("010" + String.format("%08d", id))  // 010-XXXXXXXX 형식의 전화번호
                .role(id % 2 == 0 ? UserRole.ROLE_USER : UserRole.ROLE_ADMIN)  // 짝수 ID는 USER, 홀수는 ADMIN
                .field(id % 3 == 0 ? "Backend Developer" : id % 3 == 1 ? "Frontend Developer" : "Data Scientist")  // 3가지 분야로 분배
                .isNative(id % 2 == 0)  // 짝수 ID는 true, 홀수는 false
                .serviceAccept(id % 2 == 0)  // 짝수 ID는 true, 홀수는 false
                .platformType("Mobile")  // 짝수 ID는 Web, 홀수 ID는 Mobile
                .socialType(id % 3 == 0 ? SocialType.KAKAO : id % 3 == 1 ? SocialType.GOOGLE : SocialType.NAVER)  // 3가지 SocialType 로테이션
                .socialId("social_" + id)
                .qnaBoards(new ArrayList<>())
                .qnaReplies(new ArrayList<>())
                .cart(Cart.createCart(null))
                .isDeleted(false)
                .build();
    }

    public static Member createMember(Long id) {
        Member member = createRandomMember(id);
        setId(member, id);
        return member;
    }

    public static List<Member> createMembers(List<Long> ids) {
        return ids.stream()
                .map(MemberFixture::createRandomMember)
                .collect(Collectors.toList());
    }

    private static void setId(Member member, Long id) {
        try {
            Field field = Member.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(member, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set ID via Reflection", e);
        }
    }
}