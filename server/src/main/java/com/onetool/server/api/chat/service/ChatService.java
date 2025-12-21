package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.event.MessageSavedEvent;
import com.onetool.server.api.chat.repository.ChatRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChatRepository chatRepository;
    private final RedisTemplate<String, String> chatRedisTemplate;
    private HashOperations<String, String, String> opsHashChatRoom;
    private final ApplicationEventPublisher eventPublisher;

    private static final String CHAT_ROOMS = "CHAT_ROOM";

    @PostConstruct
    private void init() {
        opsHashChatRoom = chatRedisTemplate.opsForHash();
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS).stream().map(room -> {
            try {
                String innerJson = objectMapper.readValue(room, String.class);
                return objectMapper.readValue(innerJson, ChatRoom.class);
            } catch (JsonProcessingException e) {
                log.error("채팅방 목록 조회에 실패했습니다.", e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public ChatRoom findRoomById(String roomId) {
        try {
            String roomJson = opsHashChatRoom.get(CHAT_ROOMS, roomId);
            String innerJson = objectMapper.readValue(roomJson, String.class);
            return objectMapper.readValue(innerJson, ChatRoom.class);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            log.error("ID로 채팅방을 찾는데 실패했습니다.", e);
            return null;
        }
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        try {
            opsHashChatRoom.put(CHAT_ROOMS, randomId, objectMapper.writeValueAsString(chatRoom));
        } catch (JsonProcessingException e) {
            log.error("채팅방 생성에 실패했습니다.", e);
        }
        return chatRoom;
    }

    @Transactional
    public void saveMessage(ChatMessage chatMessage) {
        chatRepository.save(chatMessage);
        eventPublisher.publishEvent(new MessageSavedEvent(chatMessage));
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findLatestMessages(final String roomId) {
        List<String> messageJsonList = chatRedisTemplate.opsForList().range("chat:room:" + roomId, 0, 199);

        if (messageJsonList == null) {
            return List.of();
        }

        return messageJsonList.stream()
                .map(messageJson -> {
                    try {
                        return objectMapper.readValue(messageJson, ChatMessage.class);
                    } catch (JsonProcessingException e) {
                        log.error("최신 메시지를 찾는데 실패했습니다.", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
