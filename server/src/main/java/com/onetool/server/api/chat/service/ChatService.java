package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatMessageQueue;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.dto.ChatMessageResponse;
import com.onetool.server.api.chat.repository.ChatRepository;
import groovy.util.logging.Slf4j;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;
    private final ChatRepository chatRepository;
    private final ChatMessageQueue messageQueue;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
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

    @Transactional
    public Long saveTextMessage(ChatMessage chatMessage) {
        ChatMessage savedChatMessage = chatRepository.save(chatMessage);
        return savedChatMessage.getId();
    }

    @Transactional
    public int saveAllTextMessage(List<ChatMessage> chatMessages) {
        List<ChatMessage> chatMessageList = chatRepository.saveAll(chatMessages);
        return chatMessageList.size();
    }

    @Transactional
    public void deleteExpiredChatMessages() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(3);
        chatRepository.deleteExpiredChatMessagesBefore(cutoff);
    }

    public ChatMessage createMessage(TextMessage message) throws JsonProcessingException {
        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        return chatMessage;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> findChatMessages(final String roomId) {
        Pageable limit = PageRequest.of(0, 50);

        List<ChatMessage> dbMessages = chatRepository.findLatestMessages(limit, roomId);
        List<ChatMessage> queueMessages = messageQueue.getQueuedMessages(roomId);

        Map<Long, ChatMessage> mergedMap = new HashMap<>();

        for (ChatMessage msg : dbMessages) {
            mergedMap.put(msg.getId(), msg);
        }

        for (ChatMessage msg : queueMessages) {
            mergedMap.put(msg.getId(), msg);
        }

        return mergedMap.values().stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt)) // BaseEntity의 시간 기준
                .toList()
                .stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }
}