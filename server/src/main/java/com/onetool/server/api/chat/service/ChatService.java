package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.repository.ChatRepository;
import com.onetool.server.global.redis.config.RedisConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;
    private final ChatRepository chatRepository;

    @Qualifier("chatRedisTemplate")
    private final RedisTemplate<String, String> chatRedisTemplate;

    // Redis Topic 관련 의존성 추가
    private final RedisMessageListenerContainer redisContainer;
    private final MessageListenerAdapter messageListener;
    private final RedisConfig redisConfig;

    // 기본 채팅방을 위한 고정 ID
    private static final String DEFAULT_ROOM_ID = "00000000-0000-0000-0000-000000000001";

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
        // 애플리케이션 시작 시, 모든 인스턴스가 동일한 ID를 가진 기본 채팅방을 생성
        ChatRoom defaultChatRoom = ChatRoom.builder()
                .roomId(DEFAULT_ROOM_ID)
                .name("기본 채팅방")
                .build();
        chatRooms.put(DEFAULT_ROOM_ID, defaultChatRoom);
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        // 채팅방이 없으면 새로 생성 (기본 채팅방 외 다른 채팅방이 필요할 경우를 대비)
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

    @Transactional
    public void saveMessage(ChatMessage chatMessage) {
        chatRepository.save(chatMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findLatestMessages(final String roomId) {
        // Redis에서 최근 메시지 200개 가져오기
        List<String> messageJsonList = chatRedisTemplate.opsForList().range("chat:room:" + roomId, 0, 199);

        if (messageJsonList == null) {
            return new ArrayList<>();
        }

        // JSON 문자열 리스트를 ChatMessage 객체 리스트로 변환
        return messageJsonList.stream()
                .map(messageJson -> {
                    try {
                        return objectMapper.readValue(messageJson, ChatMessage.class);
                    } catch (JsonProcessingException e) {
                        // 로깅 필요
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
