package com.onetool.server.api.chat.service;

import com.onetool.server.api.blueprint.Blueprint;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.MessageType;
import com.onetool.server.api.chat.dto.ChatMessageResponse;
import com.onetool.server.api.chat.repository.jpa.ChatJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatServiceTest {

    private static final String ROOM_ID = "dwadwadwadaw";
    private static final String MESSAGE = "메시지 내용~~~~~~~";
    private static final String SENDER = "ㅇㅇㅇㅇㅇㅇㅇㅇ";

    @Autowired
    private ChatJpaRepository chatRepository;
    @Autowired
    private  ChatService chatService;

    @BeforeEach
    void setUp() {
        initChatMessages();
    }

    @Test
    void findChatMessages() {
        // given
        Pageable pageable = PageRequest.of(0, 3);

        // when
        List<ChatMessage> messages = chatService.findChatMessages(ROOM_ID);
        List<ChatMessageResponse> response = messages.stream()
                .map(ChatMessageResponse::from)
                .toList();

        // then
        assertThat(response.size()).isEqualTo(3);
        for (ChatMessageResponse chatMessageResponse : response) {
            assertThat(chatMessageResponse.message()).isEqualTo(MESSAGE);
            assertThat(chatMessageResponse.sender()).isEqualTo(SENDER);
        }
    }

    private void initChatMessages() {
        chatRepository.save(createChatMessage(MessageType.ENTER));
        chatRepository.save(createChatMessage(MessageType.TALK));
        chatRepository.save(createChatMessage(MessageType.QUIT));
    }

    private ChatMessage createChatMessage(MessageType type) {
        return ChatMessage.builder()
                .message(MESSAGE)
                .type(type)
                .sender(SENDER)
                .roomId(ROOM_ID)
                .build();
    }
}