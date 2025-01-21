package com.onetool.server.api.member.enums;

public enum SocialType {
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google"),
    OTHER("other");

    private final String value;

    SocialType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}