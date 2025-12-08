package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.repository.ChatRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ChannelTopic chatTopic;

    private final RedisTemplate<String, String> chatRedisTemplate;
    private HashOperations<String, String, String> opsHashChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM";


    @PostConstruct
    private void init() {
        opsHashChatRoom = chatRedisTemplate.opsForHash();
        // 기본 채팅방 생성
        try {
            if (opsHashChatRoom.get(CHAT_ROOMS, "1") == null) {
                ChatRoom defaultChatRoom = ChatRoom.builder()
                        .roomId("1")
                        .name("기본 채팅방")
                        .build();
                opsHashChatRoom.put(CHAT_ROOMS, "1", objectMapper.writeValueAsString(defaultChatRoom));
            }
        } catch (JsonProcessingException e) {
            log.error("기본 채팅방 생성에 실패했습니다.", e);
        }
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS).stream().map(room -> {
            try {
                // 이중으로 직렬화된 JSON을 처리하기 위해, 먼저 내부 JSON 문자열을 추출
                String innerJson = objectMapper.readValue(room, String.class);
                // 추출된 JSON 문자열을 실제 ChatRoom 객체로 변환
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
            // findRoomById도 이중 직렬화 문제를 겪을 수 있으므로 동일하게 수정
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
        // Redis에 최근 메시지 저장
        try {
            String messageJson = objectMapper.writeValueAsString(chatMessage);
            chatRedisTemplate.opsForList().leftPush("chat:room:" + chatMessage.getRoomId(), messageJson);
            chatRedisTemplate.opsForList().trim("chat:room:" + chatMessage.getRoomId(), 0, 199);
        } catch (JsonProcessingException e) {
            log.error("메시지 저장에 실패했습니다.", e);
        }
    }
    
    public void publishMessage(ChatMessage chatMessage) {
        log.info("수신된 채팅 메시지: {}", chatMessage.getMessage());
        try {
            String messageJson = objectMapper.writeValueAsString(chatMessage);
            chatRedisTemplate.convertAndSend(chatTopic.getTopic(), messageJson);
        } catch (JsonProcessingException e) {
            log.error("메시지 발행에 실패했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> findLatestMessages(final String roomId) {
        // Redis에서 최근 메시지 200개 가져오기
        List<String> messageJsonList = chatRedisTemplate.opsForList().range("chat:room:" + roomId, 0, 199);

        if (messageJsonList == null) {
            return List.of();
        }

        // JSON 문자열 리스트를 ChatMessage 객체 리스트로 변환
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
