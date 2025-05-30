package com.onetool.server.global.auth.login.service;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.login.OAuthAttributes;
import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.repository.MemberJpaRepository;
import com.onetool.server.api.member.enums.SocialType;
import com.onetool.server.global.auth.login.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberJpaRepository memberJpaRepository;

    private static final String GOOGLE = "google";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes, socialType);
        MemberAuthContext context = MemberAuthContext.builder()
                .id(createdMember.getId())
                .email(createdMember.getEmail())
                .name(createdMember.getName())
                .role(createdMember.getRole().name())
                .password(createdMember.getPassword())
                .build();

        return new PrincipalDetails(context, attributes);
    }

    private SocialType getSocialType(String registrationId) {
        if(registrationId.equals(SocialType.GOOGLE.name())){
            return SocialType.GOOGLE;
        } else if(registrationId.equals(SocialType.KAKAO.name())){
            return SocialType.KAKAO;
        } else if(registrationId.equals(SocialType.NAVER.name())){
            return SocialType.NAVER;
        } else {
            return SocialType.OTHER;
        }
    }

    private Member getMember(OAuthAttributes attributes, SocialType socialType) {
        Member findMember = memberJpaRepository.findBySocialTypeAndSocialId(
                socialType, attributes.getOAuth2UserInfo().getId()).orElse(null);

        if(findMember == null) {
            return saveMember(attributes, socialType);
        }
        return findMember;
    }

    private Member saveMember(OAuthAttributes attributes, SocialType socialType) {
        Member createdMember = attributes.toEntity(socialType, attributes.getOAuth2UserInfo());
        log.info("saveMember() - email 길이 : " + createdMember.getEmail().length() );
        return memberJpaRepository.save(createdMember);
    }
}