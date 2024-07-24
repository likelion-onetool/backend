package com.onetool.server.member;

import com.onetool.server.cart.Cart;
import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.qna.QnaBoard;
import com.onetool.server.qna.QnaReply;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String password;

    @NotNull @Size(min = 1, max = 100, message = "이메일은 1 ~ 100자 이여야 합니다.") @Email
    private String email;

    @NotNull(message = "이름은 null 일 수 없습니다.") @Size(min = 1, max = 10, message = "이름은 1 ~ 10자 이여야 합니다.")
    private String name;

    @Column(name = "birth_daye") @Past
    private LocalDate birthDate;

    @Column(name = "phone_num") @Size(min = 10, max = 11)
    private String phoneNum;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ColumnDefault("'기타'")
    private String field;

    @Column(name = "is_native")
    private boolean isNative;

    @Column(name = "service_accept")
    private boolean serviceAccept;

    @Column(name = "platform_type")
    private String platformType;

    @Column(name = "social_type")
    private SocialType socialType;

    private String socialId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<QnaBoard> qnaBoards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<QnaReply> qnaReplies = new ArrayList<>();



    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cart cart;

    @Builder
    public Member(Long id, String password, String email, String name, LocalDate birthDate, String phoneNum, UserRole role, String field, boolean isNative, boolean serviceAccept, String platformType, SocialType socialType, String socialId, List<QnaBoard> qnaBoards, Cart cart) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNum = phoneNum;
        this.role = role;
        this.field = field;
        this.isNative = isNative;
        this.serviceAccept = serviceAccept;
        this.platformType = platformType;
        this.socialType = socialType;
        this.socialId = socialId;
        this.qnaBoards = qnaBoards;
        this.cart = cart;
    }
}
