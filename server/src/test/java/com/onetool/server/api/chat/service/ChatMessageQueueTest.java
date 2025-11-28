package com.onetool.server.api.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.repository.ChatRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ChatMessageQueueTest {

//    @Autowired
//    private TestChatWebSocketHandler chatWebSocketHandler;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatRepository chatRepository;

    @Mock
    private WebSocketSession webSocketSession;

    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        createChatRoom();
    }

//    @Test
//    @DisplayName("메시지 처리 시 chatMessageQueue에 메시지가 추가되는지 확인")
//    void handleTextMessageAddToQueue() throws Exception {
//        // given
//        ChatMessage chatMessage = ChatMessage.builder()
//                .roomId(ChatRoom.roomId)
//                .sender("테스트 사용자")
//                .message("테스트 메시지")
//                .type(MessageType.TALK)
//                .build();
//
//        String messageJson = objectMapper.writeValueAsString(chatMessage);
//        TextMessage textMessage = new TextMessage(messageJson);
//
//        given(chatRepository.save(any())).willReturn(chatMessage);
//
//        // when
//        chatWebSocketHandler.handleTextMessageForTest(webSocketSession, textMessage);
//
//        // then
//        assertThat(chatWebSocketHandler.getChatMessageQueue().getUnpersistedMessages())
//                .hasSize(1)
//                .first()
//                .satisfies(message -> {
//                    assertThat(message.getRoomId()).isEqualTo(chatMessage.getRoomId());
//                    assertThat(message.getSender()).isEqualTo(chatMessage.getSender());
//                    assertThat(message.getMessage()).isEqualTo(chatMessage.getMessage());
//                    assertThat(message.getType()).isEqualTo(chatMessage.getType());
//                });
//    }
//
//    @Test
//    @DisplayName("메시지가 배치 사이즈만큼 쌓이면 자동으로 저장되는지 확인")
//    void handleBatchSizeMessages() throws Exception {
//        // given
//        ArgumentCaptor<ChatMessage> messageCaptor = ArgumentCaptor.forClass(ChatMessage.class);
//
//        // when
//        for (int i = 0; i < ChatMessageQueue.getBatchSize(); i++) {
//            ChatMessage chatMessage = ChatMessage.builder()
//                    .roomId(ChatRoom.roomId)
//                    .sender("사용자" + i)
//                    .message("메시지" + i)
//                    .type(MessageType.TALK)
//                    .build();
//
//            String messageJson = objectMapper.writeValueAsString(chatMessage);
//            TextMessage textMessage = new TextMessage(messageJson);
//
//            chatWebSocketHandler.handleTextMessageForTest(webSocketSession, textMessage);
//        }
//
//        // then
//        verify(chatService).saveTextMessage(messageCaptor.capture());
//        assertThat(messageCaptor.getValue()).isNotNull();
//        assertThat(chatWebSocketHandler.getChatMessageQueue().hasEnoughMessages()).isTrue();
//    }
//
//    @Test
//    @DisplayName("입장 메시지 처리 확인")
//    void handleEnterMessage() throws Exception {
//        // given
//        ChatMessage chatMessage = ChatMessage.builder()
//                .roomId(ChatRoom.roomId)
//                .sender("새로운 사용자")
//                .type(MessageType.ENTER)
//                .build();
//
//        String messageJson = objectMapper.writeValueAsString(chatMessage);
//        TextMessage textMessage = new TextMessage(messageJson);
//
//        // when
//        chatWebSocketHandler.handleTextMessageForTest(webSocketSession, textMessage);
//
//        // then
//        assertThat(chatRoom.getSessions()).contains(webSocketSession);
//        assertThat(chatWebSocketHandler.getChatMessageQueue().getUnpersistedMessages())
//                .hasSize(1)
//                .first()
//                .satisfies(message -> {
//                    assertThat(message.getMessage()).contains("님이 입장했습니다");
//                    assertThat(message.getType()).isEqualTo(MessageType.ENTER);
//                });
//    }

    private void createChatRoom() {
        String randomId = UUID.randomUUID().toString();
        chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name("테스트 이름")
                .build();
    }
}
