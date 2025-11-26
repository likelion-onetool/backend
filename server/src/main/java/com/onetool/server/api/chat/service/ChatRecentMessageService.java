package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRecentMessageService {

    private static final int RECENT_LIMIT = 50;
    private static final String KEY_FORMAT = "chat:room:%s:messages";

    private final RedisTemplate<String, String> chatRedisTemplate;
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    public void pushRecentMessage(ChatMessage chatMessage) {
        String key = keyOf(chatMessage.getRoomId());
        try {
            String json = objectMapper.writeValueAsString(chatMessage);
            chatRedisTemplate.opsForList().leftPush(key, json);
            chatRedisTemplate.opsForList().trim(key, 0, RECENT_LIMIT - 1);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ChatMessage> getRecentMessages(String roomId) {
        String key = keyOf(roomId);
        List<String> values = chatRedisTemplate.opsForList().range(key, 0, RECENT_LIMIT - 1);

        if (values != null && !values.isEmpty()) {
            return values.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, ChatMessage.class);
                        } catch (JsonProcessingException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        }

        List<ChatMessage> fromDb = chatService.findChatMessages(roomId);

        if (!fromDb.isEmpty()) {
            List<String> jsons = fromDb.stream().map(m -> {
                try { return objectMapper.writeValueAsString(m); }
                catch (JsonProcessingException e) { return null; }
            }).filter(Objects::nonNull).toList();

            if (!jsons.isEmpty()) {
                chatRedisTemplate.opsForList().leftPushAll(key, jsons);
                chatRedisTemplate.opsForList().trim(key, 0, RECENT_LIMIT - 1);
            }
        }

        return fromDb;
    }

    private String keyOf(String roomId) {
        return String.format(KEY_FORMAT, roomId);
    }
}
