package com.onetool.server.api.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.domain.MessageType;
import com.onetool.server.api.chat.service.ChatRecentMessageService;
import com.onetool.server.api.chat.service.ChatService;
import com.onetool.server.api.chat.service.mq.ChatProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatWebSocketHandlerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    @MockBean
    private ChatProducerService chatProducerService;

    @MockBean
    private ChatRecentMessageService chatRecentMessageService;

    private BlockingQueue<String> messages = new LinkedBlockingQueue<>();
    private ChatRoom chatRoom;
    private ChatMessage talkMessage;
    private ChatMessage enterMessage;
    private ChatMessage quitMessage;

    @BeforeEach
    void setUp() {
        String roomId = UUID.randomUUID().toString();
        String name = "test-room";
        chatRoom = ChatRoom.create(name);
        chatRoom.setRoomId(roomId);

        talkMessage = ChatMessage.builder()
                .type(MessageType.TALK)
                .roomId(roomId)
                .sender("test-sender")
                .message("test-message")
                .build();

        enterMessage = ChatMessage.builder()
                .type(MessageType.ENTER)
                .roomId(roomId)
                .sender("enter-user")
                .message("")
                .build();

        quitMessage = ChatMessage.builder()
                .type(MessageType.QUIT)
                .roomId(roomId)
                .sender("quit-user")
                .message("")
                .build();

        given(chatService.findRoomById(any())).willReturn(chatRoom);
    }

    @Test
    @DisplayName("TALK 타입 메시지 테스트")
    void handleTalkMessage() throws Exception {
        // given
        given(chatService.createMessage(any())).willReturn(talkMessage);
        WebSocketSession session = connectWebSocket(chatRoom.getRoomId());
        messages.poll(5, TimeUnit.SECONDS); // Connection Established 메시지 소비

        // when
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(talkMessage)));
        String receivedMessage = messages.poll(5, TimeUnit.SECONDS);

        // then
        assertThat(receivedMessage).isNotNull();
        ChatMessage received = objectMapper.readValue(receivedMessage, ChatMessage.class);
        assertThat(received.getMessage()).isEqualTo(talkMessage.getMessage());
        verify(chatProducerService).sendMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("ENTER 타입 메시지 테스트")
    void handleEnterMessage() throws Exception {
        // given
        given(chatService.createMessage(any())).willReturn(enterMessage);
        WebSocketSession session = connectWebSocket(chatRoom.getRoomId());
        messages.poll(5, TimeUnit.SECONDS); // Connection Established 메시지 소비

        // when
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(enterMessage)));
        String receivedMessage = messages.poll(5, TimeUnit.SECONDS);

        // then
        assertThat(receivedMessage).isNotNull();
        ChatMessage received = objectMapper.readValue(receivedMessage, ChatMessage.class);
        assertThat(received.getMessage()).contains("님이 입장했습니다.");
        assertThat(chatRoom.getSessions()).hasSize(1);
    }

    @Test
    @DisplayName("QUIT 타입 메시지 테스트")
    void handleQuitMessage() throws Exception {
        // given
        given(chatService.createMessage(any())).willReturn(quitMessage);
        WebSocketSession session = connectWebSocket(chatRoom.getRoomId());
        messages.poll(5, TimeUnit.SECONDS); // Connection Established 메시지 소비
        chatRoom.getSessions().add(session); // 미리 세션에 추가

        // when
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(quitMessage)));
        String receivedMessage = messages.poll(5, TimeUnit.SECONDS);

        // then
        assertThat(receivedMessage).isNotNull();
        ChatMessage received = objectMapper.readValue(receivedMessage, ChatMessage.class);
        assertThat(received.getMessage()).contains("님이 퇴장했습니다.");
        assertThat(chatRoom.getSessions()).isEmpty();
    }

    private WebSocketSession connectWebSocket(String roomId) throws Exception {
        return new StandardWebSocketClient()
                .execute(new TextWebSocketHandler() {
                    @Override
                    public void handleTextMessage(WebSocketSession session, TextMessage message) {
                        messages.add(message.getPayload());
                    }
                }, "ws://localhost:" + port + "/ws/chat/" + roomId)
                .get(1, TimeUnit.SECONDS);
    }
}
