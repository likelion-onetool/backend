package com.onetool.server.api.chat.repository;

import com.onetool.server.api.chat.domain.ChatOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatOutboxRepository extends JpaRepository<ChatOutbox, Long> {
    List<ChatOutbox> findAllByStatus(ChatOutbox.OutboxStatus status);
}
