package com.onetool.server.api.chat.service;

import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatMessageQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatProcessService {

    private final ChatMessageQueue chatMessageQueue;
    private final ChatService chatService;

    @Async("chatBatchExecutor")
    public void processMessageQueue() {
        List<ChatMessage> unpersistedMessages = chatMessageQueue.getUnpersistedMessages();
        if (unpersistedMessages.isEmpty()) {
            return;
        }

        try {
            // 배치 사이즈만큼만 처리
            List<ChatMessage> messagesToPersist = unpersistedMessages.stream()
                    .limit(ChatMessageQueue.getBatchSize())
                    .collect(Collectors.toList());

            // DB에 저장
            Integer messageSize = chatService.saveAllTextMessage(messagesToPersist);
            log.info("Messages in Queue are saved. size: {}", messageSize);

            // 저장된 메시지 표시 및 큐에서 제거
            chatMessageQueue.markMessagesAsPersisted(messagesToPersist);

        } catch (Exception e) {
            log.error("배치 메시지 저장 중 오류 발생", e);
        }
    }
}
