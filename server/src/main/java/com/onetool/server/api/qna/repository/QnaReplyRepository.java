package com.onetool.server.api.qna.repository;

import com.onetool.server.api.qna.QnaReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QnaReplyRepository extends JpaRepository<QnaReply, Long> {

    @Query("SELECT qr FROM QnaReply qr JOIN FETCH qr.qnaBoard JOIN FETCH qr.member WHERE qr.id = :id")
    Optional<QnaReply> findByIdWithBoardAndMember(@Param("id") Long id);
}