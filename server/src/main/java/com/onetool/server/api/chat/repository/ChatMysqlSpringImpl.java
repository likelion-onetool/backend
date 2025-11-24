package com.onetool.server.api.chat.repository;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.repository.jpa.ChatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class ChatMysqlSpringImpl implements ChatRepository {

    private final ChatJpaRepository delegate;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return delegate.save(chatMessage);
    }

    @Override
    public List<ChatMessage> saveAll(List<ChatMessage> chatMessages) {
        return delegate.saveAll(chatMessages);
    }

    @Override
    public void deleteExpiredChatMessagesBefore(LocalDateTime cutoff) {
        delegate.deleteExpiredChatMessagesBefore(cutoff);
    }

    @Override
    public List<ChatMessage> findLatestMessages(Pageable pageable, String roomId) {
        return delegate.findLatestMessages(pageable, roomId);
    }
} 