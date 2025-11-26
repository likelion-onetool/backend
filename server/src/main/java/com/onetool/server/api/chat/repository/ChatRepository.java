package com.onetool.server.api.chat.repository;

import com.onetool.server.api.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


public interface ChatRepository {

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> saveAll(List<ChatMessage> chatMessages);

    List<ChatMessage> findLatestMessages(String roomId);
}
