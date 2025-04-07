package com.onetool.server.api.member.service;

import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.fake.FakeMemberJpaRepository;
import com.onetool.server.api.member.fixture.MemberFixture;
import com.onetool.server.global.new_exception.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberServiceTest {

    private MemberService memberService;
    private FakeMemberJpaRepository memberRepository;
    private PasswordEncoder encoder;

    private Member member;
    private Cart cart;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberJpaRepository();
        memberService = new MemberService(memberRepository);
        encoder = new BCryptPasswordEncoder();

        member = MemberFixture.createMember();
        cart = Cart.createCart(member);
        member.update(cart);
    }

    @Test
    void 이름과_전화번호로_회원을_조회한다() {
        // given
        memberRepository.save(member);

        // when
        String memberEmail = memberService.findOne(member.getName(), member.getPhoneNum());

        // then
        assertThat(memberEmail).isEqualTo(member.getEmail());
    }

    @Test
    void 잘못된_이름과_전화번호로_회원을_조회시_예외가_발생한다() {
        // given
        memberRepository.save(member);

        final String wrongName = "아무개";
        final String wrongPhoneNum = "01033456789";

        // when
        // then
        assertThatThrownBy(() -> memberService.findOne(wrongName, wrongPhoneNum))
                .isInstanceOf(ApiException.class)
                .extracting("customErrorMessage")
                .isEqualTo("이름과 비밀번호가 일치하는 회원이 존재하지 않습니다.");
    }

    @Test
    void 이메일로_회원을_조회한다() {
        // given
        memberRepository.save(member);

        // when
        Member foundMember = memberService.findOne(member.getEmail());

        // then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void 잘못된_이메일로_회원을_조회시_예외가_발생한다() {
        // given
        memberRepository.save(member);
        final String wrongEmail = "aldlld231@gmail.com";

        // when
        // then
        assertThatThrownBy(() -> memberService.findOne(wrongEmail))
                .isInstanceOf(ApiException.class)
                .extracting("customErrorMessage")
                .isEqualTo("이메일과 일치하는 회원이 존재하지 않습니다.");
    }

    @Test
    void ID로_회원을_조회한다() {
        // given
        memberRepository.save(member);

        // when
        Member foundMember = memberService.findOne(member.getId());

        // then
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void 잘못된_ID로_회원을_조회시_예외가_발생한다() {
        // given
        memberRepository.save(member);
        final Long wrongId = 10L;

        // when
        // then
        assertThatThrownBy(() -> memberService.findOne(wrongId))
                .isInstanceOf(ApiException.class)
                .extracting("customErrorMessage")
                .isEqualTo("ID가 일치하는 회원이 존재하지 않습니다.");
    }

    @Test
    void 회원을_삭제한다() {
        // given
        memberRepository.save(member);

        // when
        memberService.delete(member);

        // then
        assertThat(memberRepository.findById(member.getId()).isPresent()).isEqualTo(false);
    }

    @Test
    void 회원을_저장한다() {
        // given
        // when
        memberService.save(member);

        // then
        assertThat(memberRepository.findById(member.getId()).isPresent()).isEqualTo(true);
    }

    @Test
    void 회원의_비밀번호를_변경한다() {
        // given
        final String encodedNewPassword = encoder.encode("hello123");

        // when
        memberService.updatePassword(member, encodedNewPassword);

        // then
        String foundPassword = memberService.findOne(member.getId()).getPassword();
        assertThat(foundPassword).isEqualTo(encodedNewPassword);
    }

    @Test
    void ID를_가진_회원이_없는_경우_예외가_발생한다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> memberService.validateExistId(member.getId()))
                .isInstanceOf(ApiException.class)
                .extracting("customErrorMessage")
                .isEqualTo("ID가 일치하는 회원이 존재하지 않습니다.");
    }

    @Test
    void 이메일이_중복되는경우_예외가_발생한다() {
        // given
        memberRepository.save(member);

        // when
        // then
        assertThatThrownBy(() -> memberService.validateDuplicateEmail(member.getEmail()))
                .isInstanceOf(ApiException.class)
                .extracting("customErrorMessage")
                .isEqualTo("이미 사용 중인 이메일입니다.");
    }

    @Test
    void 회원ID로_장바구니와_회원_정보를_조회한다() {
        // given
        memberRepository.save(member);

        // when
        Member foundMember = memberService.findOneWithCart(member.getId());

        // then
        assertThat(foundMember.getCart()).isNotNull();
    }
}