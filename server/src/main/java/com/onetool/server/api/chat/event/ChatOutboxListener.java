package com.onetool.server.api.chat.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatOutbox;
import com.onetool.server.api.chat.repository.ChatOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatOutboxListener {

    private final ChatOutboxRepository chatOutboxRepository;
    private final RedisTemplate<String, String> chatRedisTemplate;
    private final ChannelTopic channelTopic;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(MessageSavedEvent event) {
        try {
            ChatMessage message = event.getChatMessage();
            String payload = objectMapper.writeValueAsString(message);

            ChatOutbox outbox = ChatOutbox.builder()
                    .roomId(message.getRoomId())
                    .payload(payload)
                    .status(ChatOutbox.OutboxStatus.PENDING)
                    .build();

            chatOutboxRepository.save(outbox);
            log.info("Outbox 저장 완료: {}", message.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Outbox 저장 실패", e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishToRedis(MessageSavedEvent event) {
        try {
            ChatMessage message = event.getChatMessage();
            String payload = objectMapper.writeValueAsString(message);

            chatRedisTemplate.convertAndSend(channelTopic.getTopic(), payload);
            chatRedisTemplate.opsForList().leftPush("chat:room:" + message.getRoomId(), payload);
            chatRedisTemplate.opsForList().trim("chat:room:" + message.getRoomId(), 0, 199);

            log.info("Message published to Redis: {}", message.getId());
        } catch (Exception e) {
            log.error("Redis 발행 실패 (스케줄러 재시도 예정)", e);
        }
    }
}
