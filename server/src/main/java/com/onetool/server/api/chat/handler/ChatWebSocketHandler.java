package com.onetool.server.api.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.MessageType;
import com.onetool.server.api.chat.service.ChatRecentMessageService;
import com.onetool.server.api.chat.service.ChatService;
import com.onetool.server.api.chat.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRecentMessageService chatRecentMessageService;
    @Qualifier("chatRedisTemplate")
    private final RedisTemplate<String, String> chatRedisTemplate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomId(session);
        if (roomId == null) {
            log.error("Room ID is not found in the URI.");
            session.close(CloseStatus.BAD_DATA.withReason("Room ID is required"));
            return;
        }

        // 연결 종료 시 재사용을 위해 세션 속성에 roomId 저장
        session.getAttributes().put("roomId", roomId);

        chatService.addUserToRoom(roomId, session);
        chatService.addSubscriber(roomId);

        log.info("Client {} connected to room {}", session.getId(), roomId);
        chatRecentMessageService.getRecentMessages(roomId);

        ChatMessage enterMessage = ChatMessage.builder()
                .type(MessageType.ENTER)
                .roomId(roomId)
                .sender(session.getId())
                .message(session.getId() + "님이 입장했습니다.")
                .build();

        try {
            redisPublisher.publish(enterMessage);
        } catch (Exception e) {
            log.error("Failed to publish enter message to Redis. RoomId: {}, SessionId: {}", roomId, session.getId(), e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            ChatMessage chatMessage = chatService.createMessage(message);
            chatService.saveMessage(chatMessage);

            String roomId = chatMessage.getRoomId();
            String redisKey = "chat:room:" + roomId;
            String messageJson = objectMapper.writeValueAsString(chatMessage);
            chatRedisTemplate.opsForList().leftPush(redisKey, messageJson);
            chatRedisTemplate.opsForList().trim(redisKey, 0, 199);

            try {
                redisPublisher.publish(chatMessage);
            } catch (Exception e) {
                log.error("Failed to publish chat message to Redis, but it is saved in DB. Message: {}", chatMessage, e);
            }

        } catch (Exception e) {
            log.error("Failed to process chat message. SessionId: {}", session.getId(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 연결 수립 시 저장해둔 roomId를 가져옴
        String roomId = (String) session.getAttributes().get("roomId");

        if (roomId == null) {
            return;
        }

        chatService.removeUserFromRoom(session);
        log.info("Client {} disconnected from room {}. Status: {}", session.getId(), roomId, status);

        ChatMessage quitMessage = ChatMessage.builder()
                .type(MessageType.QUIT)
                .roomId(roomId)
                .sender(session.getId())
                .message(session.getId() + "님이 퇴장했습니다.")
                .build();
        
        // 퇴장 메시지는 DB 저장 없이 발행만 함 (정책에 따라 변경 가능)
        try {
            redisPublisher.publish(quitMessage);
        } catch (RedisConnectionFailureException e) {
            log.warn("Could not connect to Redis to publish quit message. The server will continue running. RoomId: {}, SessionId: {}", roomId, session.getId());
        } catch (Exception e) { // 그 외 다른 예외 처리
            log.error("Failed to publish quit message to Redis. RoomId: {}, SessionId: {}", roomId, session.getId(), e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Transport error for session {}: {}", session.getId(), exception.getMessage());
    }

    private String getRoomId(WebSocketSession session) {
        if (session.getUri() == null) {
            return null;
        }
        // getUriTemplateVariables()는 Spring 5.0 이상에서 지원되므로, 하위 버전과 호환되는 방식으로 변경합니다.
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();

        // URI 경로가 /ws/chat/{roomId} 이므로, 마지막 경로 세그먼트를 roomId로 간주합니다.
        java.util.List<String> pathSegments = uriComponents.getPathSegments();
        return pathSegments.get(pathSegments.size() - 1);
    }
}
