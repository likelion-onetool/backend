package com.onetool.server.api.chat.repository.jpa;

import com.onetool.server.api.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatJpaRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage save(ChatMessage chatMessage);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM chat_message WHERE created_at < :cutoff", nativeQuery = true)
    void deleteExpiredChatMessagesBefore(@Param("cutoff") LocalDateTime cutoff);

    List<ChatMessage> findTop50ByRoomIdOrderByCreatedAtDesc(String roomId);
}
