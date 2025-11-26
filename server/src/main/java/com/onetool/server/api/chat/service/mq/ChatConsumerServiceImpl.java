package com.onetool.server.api.chat.service.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.dto.ChatMessagePayload;
import com.onetool.server.api.chat.dto.ChatMessageResponse;
import com.onetool.server.api.chat.service.ChatRecentMessageService;
import com.onetool.server.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatConsumerServiceImpl implements ChatConsumerService{

    private final ChatRecentMessageService chatRecentMessageService;
    private final ChatService chatService;

    private static final int BATCH_SIZE = 10;
    private final List<ChatMessage> buffer = new ArrayList<>();

    @Override
    public synchronized void consume(ChatMessagePayload payload) throws JsonProcessingException {
        chatRecentMessageService.pushRecentMessage(payload.toChatMessage());
        buffer.add(payload.toChatMessage());

        if (buffer.size() >= BATCH_SIZE) {
            flush();
        }
    }

    private void flush() {
        if (buffer.isEmpty()) return;
        List<ChatMessage> batch = new ArrayList<>(buffer);
        buffer.clear();

        Integer saved = chatService.saveAllTextMessage(batch);
        log.info("Saved batch message. size={}", saved);
    }
}
