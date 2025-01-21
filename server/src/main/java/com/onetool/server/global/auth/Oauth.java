package com.onetool.server.global.auth;

import com.onetool.server.member.domain.Member;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "access_token") @Nullable
    private String accessToken;
    @Column(name = "refresh_token") @Nullable
    private String refreshToken;
    @Column(name = "expired_date") @Nullable
    private LocalDateTime expiredDate;
    @ManyToOne
    private Member member;

    @Builder
    private Oauth(@Nullable String accessToken, @Nullable String refreshToken, @Nullable LocalDateTime expiredDate) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredDate = expiredDate;
    }

    //TODO 빌더 패턴 완성
}
