package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    private static final String DEDUP_KEY_PREFIX = "dedup:msg:";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = new String(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            Boolean isNew = redisTemplate.opsForValue()
                    .setIfAbsent(DEDUP_KEY_PREFIX + chatMessage.getId(), "1", Duration.ofSeconds(20));

            if (Boolean.FALSE.equals(isNew)) {
                log.warn("중복 메시지 필터링됨: {}", chatMessage.getId());
                return;
            }

            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (IOException e) {
            log.error("Error processing message", e);
        }
    }
}
