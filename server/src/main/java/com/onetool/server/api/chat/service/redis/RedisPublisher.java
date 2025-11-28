package com.onetool.server.api.chat.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisPublisher(@Qualifier("chatRedisTemplate") RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(ChatMessage chatMessage) {
        try {
            String message = objectMapper.writeValueAsString(chatMessage);
            redisTemplate.convertAndSend("chat-room:" + chatMessage.getRoomId(), message);
        } catch (JsonProcessingException e) {
            log.error("Error serializing chat message", e);
        }
    }
}
