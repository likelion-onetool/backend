package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.repository.ChatRepository;
import com.onetool.server.global.redis.config.RedisConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;
    private final ChatRepository chatRepository;

    // Redis Topic 관련 의존성 추가
    private final RedisMessageListenerContainer redisContainer;
    private final MessageListenerAdapter messageListener;
    private final RedisConfig redisConfig;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        // 채팅방이 없으면 새로 생성
        return chatRooms.computeIfAbsent(roomId, id -> ChatRoom.builder().roomId(id).build());
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    public ChatMessage createMessage(TextMessage message) throws JsonProcessingException {
        String payload = message.getPayload();
        return objectMapper.readValue(payload, ChatMessage.class);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findLatestMessages(final String roomId) {
        return chatRepository.findLatestMessages(roomId);
    }

    /**
     * Redis Topic 구독 추가
     */
    public void addSubscriber(String roomId) {
        redisConfig.addTopic(roomId, redisContainer, messageListener);
    }

    /**
     * 채팅방에 사용자 추가
     */
    public void addUserToRoom(String roomId, WebSocketSession session) {
        ChatRoom room = findRoomById(roomId);
        room.getSessions().add(session);
        session.getAttributes().put("roomId", roomId);
    }

    /**
     * 채팅방에서 사용자 제거
     */
    public void removeUserFromRoom(WebSocketSession session) {
        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId != null) {
            ChatRoom room = findRoomById(roomId);
            room.getSessions().remove(session);
        }
    }
}
