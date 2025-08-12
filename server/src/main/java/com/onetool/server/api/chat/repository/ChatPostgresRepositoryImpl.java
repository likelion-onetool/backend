package com.onetool.server.api.chat.repository;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.repository.jpa.ChatPostgresJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Primary
@Repository("chatPostgresRepositoryImpl")
@RequiredArgsConstructor
public class ChatPostgresRepositoryImpl implements ChatRepository {

    private final ChatPostgresJpaRepository delegate;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return delegate.save(chatMessage);
    }

    @Override
    public void deleteExpiredChatMessagesBefore(LocalDateTime cutoff) {
        delegate.deleteByCreatedAtBefore(cutoff);
    }

    @Override
    public List<ChatMessage> findLatestMessages(Pageable pageable, String roomId) {
        return delegate.findByRoomIdOrderByCreatedAtDesc(roomId);
    }
} 