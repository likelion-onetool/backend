package com.onetool.server.api.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.MessageType;
import com.onetool.server.api.chat.service.ChatRecentMessageService;
import com.onetool.server.api.chat.service.ChatService;
import com.onetool.server.api.chat.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final ChatRecentMessageService chatRecentMessageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomIdFromUri(session);
        if (roomId == null) {
            log.error("Room ID is not found in the URI.");
            session.close(CloseStatus.BAD_DATA.withReason("Room ID is required"));
            return;
        }

        chatService.addUserToRoom(roomId, session);
        chatService.addSubscriber(roomId);

        log.info("Client {} connected to room {}", session.getId(), roomId);

        // 최근 메시지 가져오기 (필요 시)
        chatRecentMessageService.getRecentMessages(roomId);

        // 입장 메시지 구성 및 전송
        ChatMessage enterMessage = ChatMessage.builder()
                .type(MessageType.ENTER)
                .roomId(roomId)
                .sender(session.getId()) // 입장 주체를 명확히 하기 위해 세션 ID 등을 사용
                .message(session.getId() + "님이 입장했습니다.")
                .build();
        redisPublisher.publish(enterMessage); // 입장 메시지도 Redis로 발행하여 모든 서버에 전파
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            ChatMessage chatMessage = chatService.createMessage(message);
            // 메시지 타입에 따라 발행
            redisPublisher.publish(chatMessage);
        } catch (Exception e) {
            log.error("Error handling text message: {}", e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId == null) {
            return;
        }

        chatService.removeUserFromRoom(session);
        log.info("Client {} disconnected from room {}. Status: {}", session.getId(), roomId, status);

        // 퇴장 메시지 구성 및 전송
        ChatMessage quitMessage = ChatMessage.builder()
                .type(MessageType.QUIT)
                .roomId(roomId)
                .sender(session.getId())
                .message(session.getId() + "님이 퇴장했습니다.")
                .build();
        redisPublisher.publish(quitMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Transport error for session {}: {}", session.getId(), exception.getMessage());
    }

    private String getRoomIdFromUri(WebSocketSession session) {
        if (session.getUri() == null) return null;
        String[] pathSegments = session.getUri().getPath().split("/");
        if (pathSegments.length > 0) {
            return pathSegments[pathSegments.length - 1];
        }
        return null;
    }
}
