package com.onetool.server.api.member.domain;

import com.onetool.server.api.cart.Cart;
import com.onetool.server.api.member.dto.command.MemberUpdateCommand;
import com.onetool.server.api.member.enums.SocialType;
import com.onetool.server.api.member.enums.UserRole;
import com.onetool.server.api.order.Order;
import com.onetool.server.api.order.OrderBlueprint;
import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.api.qna.QnaBoard;
import com.onetool.server.api.qna.QnaReply;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor
@Slf4j
@Builder
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Member SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @NotNull
    @Size(min = 1, max = 100, message = "이메일은 1 ~ 100자 이여야 합니다.")
    @Email
    @Column(unique = true)
    private String email;

    @NotNull(message = "이름은 null 일 수 없습니다.")
    @Size(min = 1, max = 10, message = "이름은 1 ~ 10자 이여야 합니다.")
    private String name;

    @Past
    private LocalDate birthDate;

    @Size(min = 10, max = 11)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ColumnDefault("'기타'")
    private String field;

    private boolean isNative;
    private boolean serviceAccept;
    private String platformType;
    private SocialType socialType;
    private String socialId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<QnaBoard> qnaBoards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<QnaReply> qnaReplies = new ArrayList<>();

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "member")
    @OrderBy("createdAt DESC")
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Transactional
    public void update(MemberUpdateCommand request, PasswordEncoder encoder) {
        Optional.ofNullable(request.name()).ifPresent(this::setName);
        Optional.ofNullable(request.phoneNum()).ifPresent(this::setPhoneNum);
        Optional.ofNullable(request.developmentField()).ifPresent(this::setField);
        Optional.ofNullable(request.newPassword()).ifPresent(newPassword -> {
            log.info("new password: {}", newPassword);
            this.setPassword(encoder.encode(newPassword));
        });
    }

    public void update(String password) {
        setPassword(password);
    }

    public void update(Cart cart) {
        setCart(cart);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public List<OrderBlueprint> getOrderBlueprints() {
        return Order.getOrderItems(getOrders());
    }
}