package com.onetool.server.api.qna.repository;

import com.onetool.server.api.qna.QnaBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {

    @Query("SELECT q FROM QnaBoard q ORDER BY q.createdAt DESC")
    List<QnaBoard> findAllQnaBoardsOrderedByCreatedAt();

    @Query("SELECT q FROM QnaBoard q LEFT JOIN FETCH q.qnaReplies WHERE q.id = :id")
    Optional<QnaBoard> findByIdWithReplies(@Param("id") Long id);
}