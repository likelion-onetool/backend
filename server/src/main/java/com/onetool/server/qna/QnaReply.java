package com.onetool.server.qna;

import com.onetool.server.global.entity.BaseEntity;
import com.onetool.server.member.domain.Member;
import com.onetool.server.qna.dto.request.QnaReplyRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "qna_reply")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reply_content") @NotNull
    @Size(min = 2, max = 10, message = "내용은 2 ~ 100자 이여야 합니다.")
    private String content;

    @Column(name = "reply_writer") @NotNull
    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_board_id")
    private QnaBoard qnaBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_reply_id")
    private Member member;

    @Builder
    private QnaReply(String content) {
        this.content = content;
    }

    public static QnaReply createReply(QnaReplyRequest.PostQnaReply request){
        return QnaReply.builder()
                .content(request.content())
                .build();
    }

    public void addReplyToBoard(QnaBoard qnaBoard){
        this.qnaBoard = qnaBoard;
        qnaBoard.getQnaReplies().add(this);
    }

    public void addReplyToWriter(Member member){
        this.member = member;
        this.writer = member.getName();
        member.getQnaReplies().add(this);
    }

    public void deleteReply(){
        member.getQnaReplies().remove(this);
        qnaBoard.getQnaReplies().remove(this);
        this.member = null;
        this.qnaBoard = null;
    }

    public void updateReply(String content){
        this.content = content;
    }
}