package com.onetool.server.api.chat.repository;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.repository.jpa.ChatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMysqlSpringImpl implements ChatRepository {

    private final ChatJpaRepository delegate;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return delegate.save(chatMessage);
    }

    @Override
    public void deleteExpiredChatMessagesBefore(LocalDateTime cutoff) {
        delegate.deleteExpiredChatMessagesBefore(cutoff);
    }

    @Override
    public List<ChatMessage> findLatestMessages(String roomId) {
        return delegate.findLatestMessages(roomId);
    }
} 