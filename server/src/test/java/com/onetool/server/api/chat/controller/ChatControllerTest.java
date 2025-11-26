package com.onetool.server.api.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.api.chat.domain.ChatMessage;
import com.onetool.server.api.chat.domain.ChatRoom;
import com.onetool.server.api.chat.domain.MessageType;
import com.onetool.server.api.chat.service.ChatService;
import com.onetool.server.api.helper.MockBeanInjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest extends MockBeanInjection {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    private ChatRoom chatRoom;
    private ChatMessage chatMessage;

    @BeforeEach
    void setUp() {
        String name = "test-room";
        chatRoom = ChatRoom.create(name);

        chatMessage = ChatMessage.builder()
                .type(MessageType.TALK)
                .roomId(chatRoom.getRoomId())
                .sender("test-sender")
                .message("test-message")
                .build();
    }

    @Test
    @DisplayName("채팅방 생성 테스트")
    @WithMockUser
    void createChatRoom() throws Exception {
        // given
        String name = "test-room";
        given(chatService.createRoom(name)).willReturn(chatRoom);

        // when & then
        mockMvc.perform(post("/chat/chatroom")
                        .param("name", name)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value(chatRoom.getRoomId()))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    @DisplayName("채팅방 단일 조회 테스트")
    @WithMockUser
    void getChatRoom() throws Exception {
        // given
        given(chatService.findRoomById(chatRoom.getRoomId())).willReturn(chatRoom);

        // when & then
        mockMvc.perform(get("/chat/chatroom")
                        .param("roomId", chatRoom.getRoomId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value(chatRoom.getRoomId()))
                .andExpect(jsonPath("$.name").value(chatRoom.getName()));
    }

    @Test
    @DisplayName("채팅방 목록 조회 테스트")
    @WithMockUser
    void getChatRoomList() throws Exception {
        // given
        given(chatService.findAllRoom()).willReturn(Collections.singletonList(chatRoom));

        // when & then
        mockMvc.perform(get("/chat/chatroom/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomId").value(chatRoom.getRoomId()))
                .andExpect(jsonPath("$[0].name").value(chatRoom.getName()));
    }

    @Test
    @DisplayName("채팅 메시지 목록 조회 테스트")
    @WithMockUser
    void getChatMessages() throws Exception {
        // given
        given(chatService.findChatMessages(chatRoom.getRoomId())).willReturn(Collections.singletonList(chatMessage));

        // when & then
        mockMvc.perform(get("/chat/chat/list")
                        .param("roomId", chatRoom.getRoomId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sender").value(chatMessage.getSender()))
                .andExpect(jsonPath("$[0].message").value(chatMessage.getMessage()));
    }
}
